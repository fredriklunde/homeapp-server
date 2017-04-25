var React = require('react');
var Navigator = require('./Navigator.jsx');

var Dashboard = require('./Dashboard/index.jsx');
var Log = require('./Log/index.jsx');

//this navigation is used for configuring both the menu and the routing
var navigation = [
  { id: 1, route: 'Dashboard', path: 'Dahsboard', display: 'Dashboard', component: Dashboard },
  { id: 3, route: 'Log', path: 'log', display: 'Logs', component: Log }
];

Navigator.setNavigation(navigation);
