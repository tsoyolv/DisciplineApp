/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

// just example
import AnotherModule from './another-module-example';

import AllHabitsApp from './components/all-habit-component-app';

const root = '/api';

ReactDOM.render(
    <AllHabitsApp />,
    document.getElementById('all_habits')
);

ReactDOM.render(
    <AnotherModule />,
    document.getElementById('another_module')
);