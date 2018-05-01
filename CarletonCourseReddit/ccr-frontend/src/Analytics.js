import React, { Component } from 'react';
import ReactFC from 'react';
import './App.css';
import Request from 'superagent';
import ReactDOM from 'react-dom';
import { LineChart, Line,PieChart, Pie, Legend, Toolti } from 'recharts';

const url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/analytics/";
//const {PieChart, Pie, Legend, Tooltip} = Recharts;

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
    	<PieChart width={800} height={400}>
        <Pie data={this.state.classStats} cx={200} cy={200} outerRadius={80} fill="#8884d8" label/>
        <Pie data={this.state.postStats} cx={500} cy={200} innerRadius={40} outerRadius={80} fill="#82ca9d"/>
       </PieChart>
			);
    		
	}
}

export default Analytics;
