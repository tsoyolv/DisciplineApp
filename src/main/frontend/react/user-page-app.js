'use strict';

const React = require('react');
const client = require('./modules/client');
import User from './components/user-component'

export default class UserApp extends React.Component {

    constructor(props) {
        super(props);
        this.state = {user: {}};
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: 'http://localhost:8888/api/users/1',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response =>
            this.setState({user: user})
        );
    }

    render() {
        return <User/>;
    }
}