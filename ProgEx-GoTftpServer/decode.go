package main

func decodeCmd(buf []byte) (cmd *pktCmd, err error) {
	cmd = new(pktCmd)

	(*cmd).cmd = tftpCmd(buf[0]<<8 + buf[1])

	var i, j int
	for i = 2; i < len(buf); i++ {
		if buf[i] == 0 {
			break
		}
	}
	(*cmd).fileName = string(buf[2:i])

	for j = i + 1; j < len(buf); j++ {
		if buf[j] == 0 {
			break
		}
	}
	cmd.mode = string(buf[i+1 : j])

	return cmd, nil
}
