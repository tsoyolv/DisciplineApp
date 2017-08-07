'use strict';

const React = require('react');
const ReactDOM = require('react-dom');

import Habit from './habit-component'

class HabitList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    render() {
        var habits = this.props.habits.map(habit =>
            <Habit key={habit.entity._links.self.href}
                   habit={habit}
                   attributes={this.props.attributes}
                   onUpdate={this.props.onUpdate}
                   onDelete={this.props.onDelete}/>
        );

        var navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                <input placeholder="page size" ref="pageSize" defaultValue={this.props.pageSize}
                       onInput={this.handleInput}/>
                <table className="table table-striped">
                    <tbody>
                    <tr>
                        <th>Name</th>
                        <th>Difficulty</th>
                        <th>Description</th>
                        <th>Created when</th>
                        <th>Updated when</th>
                        <th>User</th>
                        <th></th>
                        <th></th>
                    </tr>
                    {habits}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }

    handleInput(e) {
        e.preventDefault();
        var pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }
}

export default HabitList;