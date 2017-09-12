'use strict';

const React = require('react');
const client = require('./../modules/client');

export default class HabitHistoryTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {histories:[]};
    }

    componentDidMount() {
        this.loadFromServer();
    }

    loadFromServer() {
        var href = window.location.href;
        var id = href.substr(href.lastIndexOf('\\'));
        client({
            method: 'GET',
            path: '/api/habits/' + id + '/histories',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            this.setState({histories:response.entity._embedded.items});
        }); // todo add pagination
    }

    render() {
        var outs = this.state.histories.map(it => <HabitHistory key={it._links.self.href} history={it}/>);
        return (
            <table className="table">
                <caption>History</caption>
                <thead>
                <tr>
                    <th>Completed Date</th>
                    <th>Completed</th>
                    <th>Completed (times)</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
                {outs}
                </tbody>
            </table>
        );
    }
}

class HabitHistory extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <tr>
                <td>{(new Date(this.props.history.completedDate)).toUTCString()}</td>
                <td>{this.props.history.completed?'YES':'NO'}</td>
                <td>{this.props.history.completedCount}</td>
                <td>{this.props.history.description}</td>
            </tr>
        );
    }
}