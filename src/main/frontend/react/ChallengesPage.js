'use strict';

const React = require('react');
const client = require('./modules/client');

import Navbar from './components/Navbar'
import CreateChallenge from './components/CreateChallenge'
import AvailableChallengesTable from './components/AvailableChallengesTable'

export default class ChallengesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}};
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: '/api/users/current',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            this.setState({user:response.entity});
        });
    }

    render () {
        return (
            <div>
                <Navbar user={this.state.user} />
                <div className="container">
                    <div className="row">
                        <div className="col-sm-3 col-md-2 sidebar">
                            <ul className="nav nav-sidebar">
                                <li><a href="#">Summary (Not implemented) </a></li>
                                <li><a href="/user-habit">Habits</a></li>
                                <li><a href="#">Tasks (Not implemented)</a></li>
                                <li className="active"><a href="#">Challenges <span className="sr-only">(current)</span></a></li>
                                <li><a href="#">Competition (Not implemented)</a></li>
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            <CreateChallenge />
                            <AvailableChallengesTable user={this.state.user}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}