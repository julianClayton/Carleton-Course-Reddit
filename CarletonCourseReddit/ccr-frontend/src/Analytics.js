import React, { Component } from 'react';
import ReactFC from 'react';
import './App.css';
import Request from 'superagent';
import ReactDOM from 'react-dom';
import { LineChart, Line,PieChart, Pie, Legend, Tooltip, Label } from 'recharts';

const url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/analytics/";

class Analytics extends Component {
	constructor() {
		super();
    	this.state = {
    		classStats: [],
    		postStats: []
    	};
    	this.runAnalytics()
	};

	runAnalytics() {
		Request.get(url + "classes").then((res) => {
      		this.setState({
        		classStats: res.body.stats, 
        	});
    	});

    	Request.get(url + "posts").then((res) => {
      		this.setState({
        		postStats: res.body.stats, 
        	});
    	});
	}

  render() {
    return (
    	<div className="pie-div">
    	<h3> Classes per Subject     &nbsp;&nbsp;&nbsp;        Posts per Class</h3>
    	<PieChart width={800} height={400}>
        <Pie data={this.state.classStats} cx={300} cy={100} innerRadius={40} outerRadius={80} fill="#8884d8"></Pie>
        <Pie data={this.state.postStats} cx={500} cy={100} innerRadius={40} outerRadius={80} fill="#82ca9d"/>
        <Tooltip/>
       </PieChart>
       </div>
		);	
	}
}

export default Analytics;
