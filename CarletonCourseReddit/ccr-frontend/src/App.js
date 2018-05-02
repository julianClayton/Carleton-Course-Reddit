import React, { Component } from 'react';
import logo from './raven.png';
import './App.css';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import Analytics from "./Analytics";
import CourseSearcher from "./CourseSearcher"
const url = "http://localhost:8080/CarletonCourseReddit/ccr/rest/";

class App extends Component {
  constructor() {
    super();
  }

  render() {
    return (
      <Tabs>
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <h1 className="App-title">Carleton University Course Reddit Finder</h1>
            <h2>See what students are saying about Carleton courses on Reddit! </h2>
              <TabList className="align-left">
                <Tab>Search</Tab>
                <Tab>Analytics</Tab>
              </TabList>
          </header>
              <TabPanel>
                  <CourseSearcher />
              </TabPanel>
              <TabPanel>
                    <Analytics />
              </TabPanel>
          </div>
        </Tabs>
      );
  }
}

export default App;
