import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Request from 'superagent';
import _ from 'lodash';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
var url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/";



class App extends Component {
  constructor() {
    super();
    this.state = {
      subjs: [],
      showMenu: false,
      updateValue: '',
      selectedSubj: '',
    };
    this.getClasses();
    this.updateValue = this.updateValue.bind(this)
  }
  getClasses() {
    Request.get(url).then((res) => {
      this.setState({
        subjs: res.body, 
        });
    });
  }


  updateValue(newValue) {
    this.setState({
      selectValue: newValue,
    });
    Request.get(url + "courses/categories/"+ newValue.value).then((res) => {
      console.info(res.body);
      this.setState({
        selectedSubj:res.body
        });
    });
  }

  render() {

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
      </div>
    );
  }
}

export default App;
