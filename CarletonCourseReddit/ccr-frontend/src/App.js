import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Request from 'superagent';
import _ from 'lodash';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import TreeView from 'react-treeview';
var url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/";

class App extends Component {
  constructor() {
    super();

    this.state = {
      subjs: [],
      showMenu: false,
      updateValue: '',
      selectedSubj: '',
      collapsedBookkeeping: [].map(() => true),
      courses: [],
    };


    this.getClasses();
    this.updateValue = this.updateValue.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.collapseAll = this.collapseAll.bind(this);
  }
  getClasses() {
    Request.get(url).then((res) => {
      this.setState({
        subjs: res.body, 
        });
    });
  }
  handleClick(i) {
    let [...collapsedBookkeeping] = this.state.collapsedBookkeeping;
    collapsedBookkeeping[i] = !collapsedBookkeeping[i];
    this.setState({collapsedBookkeeping: collapsedBookkeeping});
  }

  collapseAll() {
    this.setState({
      collapsedBookkeeping: this.state.collapsedBookkeeping.map(() => false),
    });
  }


  updateValue(newValue) {
    this.setState({
      selectValue: newValue,
    });
    Request.get(url + "courses/categories/"+ newValue.value).then((res) => {
      this.setState({
        selectedSubj:res.body,
        courses: res.body.courses,
        });
    });
  }

  render() {
    const collapsedBookkeeping = this.state.collapsedBookkeeping;
    var subjects = _.map(this.state.subjs, (subject) => {
      return { value: subject, label: subject}
    });

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Carleton University Course Reddit Indexer</h1>
        </header>
        <div>
          <Select
          name="subject"
          value={this.state.selectValue}
          onChange={this.updateValue}
          options={subjects}
          />
        </div>
        <div>
        <button onClick={this.collapseAll}>Collapse all</button>
        {this.state.courses.map((node, i) => {
          const course = node.courseName;
          const label =
            <span className="node" onClick={this.handleClick.bind(null, i)}>
              {course}, sentiment : {node.sentiment}
        </span>
        return (
            <TreeView nodeLabel={label} key={i} collapsed={!collapsedBookkeeping[i]} onClick={this.handleClick.bind(null, i)}>
                {node.posts.map(post => {
                  return <div><span className="node">{post.text}</span></div>
                })
              }
            </TreeView>
          );
        })}
        </div>
      </div>
    );
  }
}

export default App;
