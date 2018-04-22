import React, { Component } from 'react';
import logo from './raven.png';
import './App.css';
import Request from 'superagent';
import _ from 'lodash';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import TreeView from 'react-treeview';
import SplitterLayout from 'react-splitter-layout';
import Divider from 'muicss/lib/react/divider';

var url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/";

class App extends Component {
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


  updateValue(newValue) {
    if (newValue !== null) {
      Request.get(url + "courses/categories/"+ newValue.value).then((res) => {
        this.setState({
          selectedSubj:res.body,
          courses: res.body.courses,
          selectValue: newValue,
          });
      });
    } else {

    }
  }

  render() {
    const collapseTree = this.state.collapseTree;
    var subjects = _.map(this.state.subjs, (subject) => {
      return { value: subject, label: subject}
    });

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Carleton University Course Reddit Finder</h1>
          <h2>See what students are saying about Carleton courses on Reddit! </h2>
        </header>
        <div>
          <Select
            name="subject"
            value={this.state.selectValue}
            onChange={this.updateValue}
            options={subjects}/>
        </div>
        <div>
        <button onClick={this.collapseAll} className="mybutton">Collapse all</button>
        <div className="topdiv">
        {this.state.courses.map((node, i) => {
          const course = node.courseName;
          const label = <span className="topnode" onClick={this.handleCourseClick.bind(null, i)}>{course}, sentiment : {node.sentiment}</span>
        return (
            <TreeView nodeLabel={label} key={i} collapsed={!collapseTree[i]} onClick={this.handleCourseClick.bind(null, i)}>
                {node.posts.map((post, j) => {return <div><span className="node" onClick={this.handleCommentClick.bind(null,post)} key={j}>{post.text}</span><hr></hr></div>})}
            </TreeView>);
        })}
        </div>
        </div>
      </div>
    );
  }
}

export default App;
