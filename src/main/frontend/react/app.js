/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';
const React = require('react');
const ReactDOM = require('react-dom');

import UserApp from './user-page-app';
import AllHabitsApp from './all-habits-page-app';
import UserEditApp from './UserEditPage';
import UserHabitsPage from './UserHabitsPage';

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

if(document.getElementById('user_edit')) {
    ReactDOM.render(
        <UserEditApp />,
        document.getElementById('user_edit')
    );
}

if(document.getElementById('user_habits')) {
    ReactDOM.render(
        <UserHabitsPage />,
        document.getElementById('user_habits')
    );
}