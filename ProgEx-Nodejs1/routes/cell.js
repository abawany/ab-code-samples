var express = require('express');
var router = express.Router();
var redis = require('redis');

var rdsCli = redis.createClient();
rdsCli.on('error', function(err){
	console.log({"Err-Rds": err});
});

router.get('/:key', function(req, res) {
	var key = req.params.key;
	console.log({key: key});
	
	rdsCli.get(key, function(err, val) {
		console.log({err: err, val: val});
		if (err) { res.send(500, ''); return; }
		if (val === null || val.length === 0) { res.send(404, ''); return; }
		res.send(200, {val: val});
		return;
	});
});

router.put('/', function(req, res) {
	var key = req.body.key;
	var val = req.body.val;
	console.log({body: req.body, key: key, val: val});
	
	rdsCli.set(key, val, function(err, rsp) {
		console.log({err: err, rsp: rsp});
		if (err) { res.send(500, ''); return; }
		res.send(204, '');
		return;
	});
});

module.exports = router;