/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

import AllHabitsApp from './all-habits-page-app';

const root = '/api';

ReactDOM.render(
    <AllHabitsApp />,
    document.getElementById('all_habits')
);