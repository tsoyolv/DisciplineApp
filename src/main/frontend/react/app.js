/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';
const React = require('react');
const ReactDOM = require('react-dom');
const root = '/api';

import UserApp from './user-page-app';
import AllHabitsApp from './all-habits-page-app';

if(document.getElementById('all_habits')) {
    ReactDOM.render(
        <AllHabitsApp />,
        document.getElementById('all_habits')
    );
}

if(document.getElementById('user')) {
    ReactDOM.render(
        <UserApp />,
        document.getElementById('user')
    );
}