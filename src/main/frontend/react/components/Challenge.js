'use strict';

const React = require('react');

export default class Challenge extends React.Component {
    constructor(props) {
        super(props);
    }

    render () {
        return (
        <div className="row">
            <div className=" col-md-9 col-lg-9 hidden-xs hidden-sm">
                <h1 className="page-header">{this.props.challenge.name}</h1>
                <div className="row nav-row">
                    <button data-toggle="collapse" data-target="#challengeInfo" type="button" className="btn btn-primary">Show/hide challenge info</button>
                </div>
                <div id="challengeInfo" className="collapse">
                <table className="table table-user-information">
                    <tbody>
                    <tr>
                        <td>Difficulty:</td>
                        <td><strong>{this.props.challenge.difficulty}</strong></td>
                    </tr>
                    <tr>
                        <td>Sphere:</td>
                        <td><strong>{this.props.challenge.sphere}</strong></td>
                    </tr>
                    <tr>
                        <td>Type :</td>
                        <td><strong>{this.props.challenge.type}</strong></td>
                    </tr>
                    <tr>
                        <td>Description:</td>
                        <td><strong>{this.props.challenge.description}</strong></td>
                    </tr>
                    <tr>
                        <td>Votes:</td>
                        <td><strong>{this.props.challenge.votes}</strong></td>
                    </tr>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
        );
    }
}