'use strict';

const React = require('react');
const client = require('./modules/client');

import Navbar from './components/Navbar'
import Habit from './components/Habit'
import HabitHistoryTable from './components/HabitHistoryTable'

export default class HabitPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {habit:{}};
    }

    componentDidMount() {
        this.loadFromServer();
    }

    loadFromServer() {
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/habits/' + id,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => this.setState({habit:response.entity}))
    }

    render () {
        return (
            <div>
                <Navbar/>
                <div className="container">
                    <div className="row">
                        <div className="col-sm-3 col-md-2 sidebar">
                            <ul className="nav nav-sidebar">
                                <li><a href="#">Summary (Not implemented) </a></li>
                                <li><a href="/habit">All Habits</a></li>
                                <li><a href="/user-habit">Habits</a></li>
                                <li><a href="#">Tasks (Not implemented)</a></li>
                                <li><a href="#">Challenges (Not implemented)</a></li>
                                <li><a href="#">Competition (Not implemented)</a></li>
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            <Habit habit={this.state.habit} />
                            <HabitHistoryTable habit={this.state.habit}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}