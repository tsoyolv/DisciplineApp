'use strict';

const React = require('react');
const client = require('./modules/client');

const GET_USER_PATH = '/api/users';

import User from './components/user-component'

export default class UserApp extends React.Component {

    constructor(props) {
        super(props);
        this.state = {user: {}};
    }

    componentDidMount() {
        this.setCurrentUserToState();
    }

    setCurrentUserToState () {
        this.sendRequest('GET', GET_USER_PATH, function (response) {
            this.setState({user: response.entity});
        });
    }

    sendRequest(method, path, callback) {
        client({
            method: method,
            path: path,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            if (callback) { callback(response);}
            }
        );
    }

    render() {
        var t = this.state.user;
        var username = t.username;
        return <User username={username}/>;
    }
}