'use strict';

const React = require('react');

import User from './components/user-component'

export default class UserApp extends React.Component {

    constructor(props) {
        super(props);
        this.state = {user: {}};
    }

    /* todo
    follow(client, root, [
        {rel: 'habits', params: {size: pageSize}}]
    )
*/
    render() {
        return <User/>;
    }
}