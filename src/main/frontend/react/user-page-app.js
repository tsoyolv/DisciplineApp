'use strict';

const React = require('react');
const client = require('./modules/client');
const when = require('when');

const GET_USER_PATH = '/api/users/current';

import User from './components/User'

export default class UserApp extends React.Component {

    static httpGET(path) {
        return client({
            method: 'GET',
            path: path,
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        })
    }

    constructor(props) {
        super(props);
        this.state = {user: {}};
    }

    componentDidMount() {
        UserApp.httpGET(GET_USER_PATH).done(response => { this.setState({user:response.entity}); });
    }

    render() {
        return <User user={this.state.user}/>;
    }
}