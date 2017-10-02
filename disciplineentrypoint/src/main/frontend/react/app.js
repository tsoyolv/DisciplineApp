/**
 * TcoiOleg on 21.05.2017.
 */
'use strict';
const React = require('react');
const ReactDOM = require('react-dom');

import UserPage from './UserPage';
import AllHabitsApp from './all-habits-page-app';
import UserEditPage from './UserEditPage';
import UserHabitsPage from './UserHabitsPage';
import HabitPage from './HabitPage';
import ChallengesPage from './ChallengesPage';
import ChallengePage from './ChallengePage';
import Summary from './Summary';

/* todo react routing */

if(document.getElementById('all_habits')) {
    ReactDOM.render(
        <AllHabitsApp />,
        document.getElementById('all_habits')
    );
}

if(document.getElementById('user')) {
    ReactDOM.render(
        <UserPage />,
        document.getElementById('user')
    );
}

if(document.getElementById('user_edit')) {
    ReactDOM.render(
        <UserEditPage />,
        document.getElementById('user_edit')
    );
}

if(document.getElementById('user_habits')) {
    ReactDOM.render(
        <UserHabitsPage />,
        document.getElementById('user_habits')
    );
}

if(document.getElementById('habit')) {
    ReactDOM.render(
        <HabitPage />,
        document.getElementById('habit')
    );
}

if(document.getElementById('challenges')) {
    ReactDOM.render(
        <ChallengesPage />,
        document.getElementById('challenges')
    );
}

if(document.getElementById('challenge')) {
    ReactDOM.render(
        <ChallengePage />,
        document.getElementById('challenge')
    );
}

if(document.getElementById('summary')) {
    ReactDOM.render(
        <Summary />,
        document.getElementById('summary')
    );
}