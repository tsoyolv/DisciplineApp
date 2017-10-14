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

/*import {
    BrowserRouter as Router,
    Route,
    Link
} from 'react-router-dom'

import './css/scroll.css';

/!* todo react routing *!/
const BasicExample = () => (
    <Router>
        <div>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/about">About</Link></li>
                <li><Link to="/topics">Topics</Link></li>
            </ul>

            <hr/>

            <Route exact path="/" component={Home}/>
            <Route path="/about" component={About}/>
            <Route path="/topics" component={Topics}/>
        </div>
    </Router>
)

const Home = () => (
    <div className="newss">
        <h2>Home</h2>
    </div>
)

const About = () => (
    <div>
        <h2>About</h2>
    </div>
)

const Topics = ({ match }) => (
    <div>
        <h2>Topics</h2>
        <ul>
            <li>
                <Link to={`${match.url}/rendering`}>
                    Rendering with React
                </Link>
            </li>
            <li>
                <Link to={`${match.url}/components`}>
                    Components
                </Link>
            </li>
            <li>
                <Link to={`${match.url}/props-v-state`}>
                    Props v. State
                </Link>
            </li>
        </ul>

        <Route path={`${match.url}/:topicId`} component={Topic}/>
        <Route exact path={match.url} render={() => (
            <h3>Please select a topic.</h3>
        )}/>
    </div>
)

const Topic = ({ match }) => (
    <div>
        <h3>{match.params.topicId}</h3>
    </div>
)

ReactDOM.render(
    <BasicExample />,
    document.getElementById('user')
);*/

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