var express = require('express');
var router = express.Router();
var redis = require('redis');

var rdsCli = redis.createClient();
rdsCli.on('error', function(err){
	console.log({"Err-Rds": err});
});

router.get('/:key', function(req, res) {
	var key = req.params.key;
	console.log({get: {key: key}});
	
	rdsCli.get(key, function(err, val) {
		console.log({err: err, val: val});
		if (err) { res.status(500).send(); return; }
		if (val === null || val.length === 0) { 
			res.status(404).send(); 
			return; 
		}
		res.status(200).send({val: val});
		return;
	});
});

router.put('/', function(req, res) {
	var key = req.body.key;
	var val = req.body.val;
	console.log({put: {key: key, val: val}});
	
	rdsCli.set(key, val, function(err, rsp) {
		console.log({err: err, rsp: rsp});
		if (err) { res.status(500).send(); return; }
		res.status(204).send();
		return;
	});
});

router.post('/:key/:val', function(req, res) {
	var key = req.params.key;
	var val = req.params.val;
	console.log({post: {key: key, val: val}});

	rdsCli.set(key, val, function(err, rsp) {
		console.log({err: err, rsp: rsp});
		if (err) { res.status(500).send(); return; }
		res.status(204).send();
		return;
	});
});

router.delete('/:key', function(req, res) {
	var key = req.params.key;
	console.log({delete: {key: key}});

	rdsCli.del(key, function(err, rsp) {
		console.log({err: err, rsp: rsp});
		if (err) { res.status(500).send(); return; }
		res.status(204).send();
		return;
	});
});

router.options('/', function(req, res) {
	console.log({options: {key: req.params.key, val: req.params.val}});
	res.status(204).send();
	return;
});

module.exports = router;
