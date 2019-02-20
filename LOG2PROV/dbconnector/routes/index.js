var express = require('express');
var axios = require('axios');
var URL = require("url").URL;
var router = express.Router();

/* GET home page. */
router.get('/metrics', function (req, res, next) {
  var resultOBJ = new Object();
  axios.all([
    getCPUdata(req.query.endpoint, req.query.startTime, req.query.endTime),
    getMEMdata(req.query.endpoint, req.query.startTime, req.query.endTime),
    getNETINdata(req.query.endpoint, req.query.startTime, req.query.endTime),
    getNETOUTdata(req.query.endpoint, req.query.startTime, req.query.endTime)
  ])
    .then(axios.spread(function (cpu, mem, netin, netout) {
      resultOBJ.cpu = filterData(cpu.data.data.result);
      resultOBJ.mem = filterData(mem.data.data.result);
      resultOBJ.net_in = filterData(netin.data.data.result);
      resultOBJ.net_out = filterData(netout.data.data.result);
      res.send(JSON.stringify(resultOBJ));
     }))
    .catch(function (error) {
      res.send('error retrieving input: \n' + error.stack);
    });
});

function filterData(data){
  var result;

  data.forEach(function(element){
    var expr = /grafana|cAdvisor|cadvisor|nodeexporter|alertmanager|caddy|prometheus|influxdb/;
    if (!expr.test(element.metric.name)) {
      result = element.values;
    }
  })

  return result;
}

function getCPUdata(endpoint, startTime, endTime) {
  var url = new URL(endpoint);

  url.pathname = '/api/v1/query_range';
  url.searchParams.set('query', 'sum(rate(container_cpu_usage_seconds_total{name=~".+"}[30m])) by (name) * 100');
  url.searchParams.append('start', startTime);
  url.searchParams.append('end', endTime);
  url.searchParams.append('step', 1);
  url.searchParams.append('timeout', '5s');

  return axios.get(url.href);
}

function getMEMdata(endpoint, startTime, endTime) {
  var url = new URL(endpoint);

  url.pathname = '/api/v1/query_range';
  url.searchParams.set('query', 'sum(container_memory_rss{name=~".+"}) by (name)');
  url.searchParams.append('start', startTime);
  url.searchParams.append('end', endTime);
  url.searchParams.append('step', 1);
  url.searchParams.append('timeout', '5s');

  return axios.get(url.href);
}

function getNETOUTdata(endpoint, startTime, endTime) {
  var url = new URL(endpoint);

  url.pathname = '/api/v1/query_range';
  url.searchParams.set('query', 'sum(rate(container_network_transmit_bytes_total{name=~".+"}[30m])) by (name)');
  url.searchParams.append('start', startTime);
  url.searchParams.append('end', endTime);
  url.searchParams.append('step', 1);
  url.searchParams.append('timeout', '5s');

  return axios.get(url.href);
}

function getNETINdata(endpoint, startTime, endTime) {
  var url = new URL(endpoint);

  url.pathname = '/api/v1/query_range';
  url.searchParams.set('query', 'sum(rate(container_network_receive_bytes_total{name=~".+"}[30m])) by (name)');
  url.searchParams.append('start', startTime);
  url.searchParams.append('end', endTime);
  url.searchParams.append('step', 1);
  url.searchParams.append('timeout', '5s');

  return axios.get(url.href);
}

module.exports = router;
