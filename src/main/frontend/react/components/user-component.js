'use strict';

const React = require('react');

class User extends React.Component {
    render() {
        return <div className="media-body">
                    <hr/>
                    <h3><strong>Bio</strong></h3>
                    <p>Something interesting</p>
                    <hr/>
                    <h3><strong>Location</strong></h3>
                    <p>Earth</p>
                    <hr/>
                    <h3><strong>Gender</strong></h3>
                    <p>Male</p>
                    <hr/>
                    <h3><strong>Birthday</strong></h3>
                    <p>January 1, 1601</p>
                </div>;
    }
}

export default User;