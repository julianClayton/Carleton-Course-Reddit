import React, { Component } from 'react';
import logo from './raven.png';
import './App.css';
import Request from 'superagent';
import _ from 'lodash';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import TreeView from 'react-treeview';

const url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/";

class CourseSearcher extends Component {
	  constructor() {
    super();

    this.state = {
      subjs: [],
      showMenu: false,
      updateValue: '',
      selectedSubj: '',
      collapseTree: [].map(() => true),
      courses: [],
    };


    this.getClasses();
    this.updateValue = this.updateValue.bind(this);
    this.handleCourseClick = this.handleCourseClick.bind(this);
    this.collapseAll = this.collapseAll.bind(this);
    this.handleCommentClick = this.handleCommentClick.bind(this);
  }


  getClasses() {
    Request.get(url).then((res) => {
      this.setState({
        subjs: res.body, 
        });
    });
  }
  handleCourseClick(i) {
    let [...collapseTree] = this.state.collapseTree;
    collapseTree[i] = !collapseTree[i];
    this.setState({collapseTree: collapseTree});
  }
  handleCommentClick(post) {
    var win = window.open(post.url, '_blank');
    win.focus();
  }

  collapseAll() {
    this.setState({
      collapseTree: this.state.collapseTree.map(() => false),
    });
  }
  classColour(i) {
    if (this.state.collapseTree[i]) {
      return "topnode-selected";
    }
    return "topnode";
  }
  updateValue(newValue) {
    if (newValue !== null) {
      this.collapseAll();
      Request.get(url + "courses/categories/"+ newValue.value).then((res) => {
        this.setState({
          selectedSubj:res.body,
          courses: res.body.courses,
          selectValue: newValue,
          });
      });
    }
  }


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
    const collapseTree = this.state.collapseTree;
    var subjects = _.map(this.state.subjs, (subject) => {
      return { value: subject, label: subject}
    });
    return (
      <div>
          <div>
            <Select
              className="select"
              class="pull-left"
              name="subject"
              value={this.state.selectValue}
              onChange={this.updateValue}
              options={subjects}
              />
              <div className="mybutton-container"><button onClick={this.collapseAll} className="mybutton">Collapse all</button></div>
          </div>
          <div>
            <div className="topdiv">
              {this.state.courses.map((node, i) => {
                const course = node.courseName;
                const label = <span  className={this.classColour(i)} onClick={this.handleCourseClick.bind(null, i)}>{course}, sentiment : {node.sentiment}</span>
              return (
                <TreeView nodeLabel={label} key={node.className} collapsed={!collapseTree[i]} onClick={this.handleCourseClick.bind(null, i)}>
                    {node.posts.map((post, j) => {return <div className="comment-div "><span className="node" onClick={this.handleCommentClick.bind(null,post)} key={j}><h3>{post.author} says:</h3><p>{post.text}</p></span><hr></hr></div>})}
                </TreeView>);})}
            </div>
          </div>
      </div>
		);	
	}
}

export default CourseSearcher;
