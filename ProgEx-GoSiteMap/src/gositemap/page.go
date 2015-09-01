package main

import (
	"fmt"
	"io"

	"golang.org/x/net/html"
)

type Page struct {
	Url    string
	Title  string
	Assets []string
	Links  []*Page
}

func NewPage(url string) (pg *Page) {
	pg = new(Page)
	pg.Url = url
	pg.Assets = make([]string, 0)
	pg.Links = make([]*Page, 0)
	return pg
}

func PageParser(stream io.Reader, pg *Page) (err error) {
	doc, err := html.Parse(stream)
	if err != nil {
		panic(err)
	}

	var f func(*html.Node)
	f = func(n *html.Node) {
		var attrs = getAttrMap(n)

		switch n.Type {
		case html.ElementNode:
			fmt.Println("elen", n.Data)

			switch n.Data {
			case "script":
				if _, ok := attrs["txtn"]; ok {
					pg.Assets = append(pg.Assets, "embedded script")
				} else if src, ok := attrs["src"]; ok {
					pg.Assets = append(pg.Assets, src)
				} else if href, ok := attrs["href"]; ok {
					pg.Assets = append(pg.Assets, href)
				}
			case "link":
				if href, ok := attrs["href"]; ok {
					pgTmp := NewPage(href)
					pg.Links = append(pg.Links, pgTmp)
				}
				fallthrough
			case "video":
				fallthrough
			case "a":
				var attrs = getAttrMap(n)
				if href, ok := attrs["href"]; ok {
					pgTmp := NewPage(href)
					pg.Links = append(pg.Links, pgTmp)
				}
			}

		case html.ErrorNode:
			fmt.Println("errn", n)

		case html.TextNode:
			fmt.Println("txtn", n.Data)

		case html.DocumentNode:
			fmt.Println("docn", n.Data)

		case html.DoctypeNode:
			fmt.Println("dctn", n.Data)

		default:
			fmt.Println("dflt", n.Data)
		}

		for chld := n.FirstChild; chld != nil; chld = chld.NextSibling {
			f(chld)
		}
	}

	f(doc)
	return nil
}

func PageParser2(stream io.Reader, pg *Page) (err error) {

	htmlTokenizer := html.NewTokenizer(stream)
loopTop:
	for {
		curTok := htmlTokenizer.Next()
		switch curTok {
		case html.ErrorToken:
			fmt.Println("Ending...")
			break loopTop

		//case html.StartTagToken:
		default:
			tagName, _ := htmlTokenizer.TagName()
			tagNameStr := string(tagName)
			tagAttrKey, tagAttrVal, _ := htmlTokenizer.TagAttr()
			tagAttrKeyStr := string(tagAttrKey)
			tagAttrValStr := string(tagAttrVal)

			//if tagNameStr == "script" || tagNameStr == "img" || tagNameStr == "a" || tagNameStr == "source" {
			fmt.Println("tn", tagNameStr, tagAttrKeyStr, tagAttrValStr)
			//}
		}
	}

	return nil
}

func getAttrMap(n *html.Node) (attrs map[string]string) {
	attrs = make(map[string]string)

	for _, attr := range n.Attr {
		fmt.Println("attr", "k", attr.Key, "V", attr.Val)
		attrs[attr.Key] = attr.Val
	}
	return attrs
}
