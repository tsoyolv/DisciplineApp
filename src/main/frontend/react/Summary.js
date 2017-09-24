'use strict';

const React = require('react');
const client = require('./modules/client');
const Highcharts = require('highcharts');

import Navbar from './components/Navbar'

export default class ChallengesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {user: {}};
        this.showCompetition = this.showCompetition.bind(this);
        this.showChallenges = this.showChallenges.bind(this);
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: '/api/users/current',
            headers: {
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            this.setState({user:response.entity});
        });
    }

    showCompetition() {
        if (this.state.user) {
            if (this.state.user.hidden != null && !this.state.user.hidden) {
                return (<li><a href="#">Competition (Not implemented)</a></li>);
            }
        }
    }

    showChallenges() {
        if (this.state.user) {
            if (this.state.user.hidden != null && !this.state.user.hidden) {
                return (<li><a href="/challenge">Challenges</a></li>);
            }
        }
    }

    render () {
        return (
            <div>
                <Navbar user={this.state.user} />
                <div className="container">
                    <div className="row">
                        <div className="col-sm-3 col-md-2 sidebar">
                            <ul className="nav nav-sidebar">
                                <li className="active"><a href="#">Summary <span className="sr-only">(current)</span></a></li>
                                <li><a href="/user-habit">Habits</a></li>
                                <li><a href="#">Tasks (Not implemented)</a></li>
                                {this.showChallenges()}
                                {this.showCompetition()}
                            </ul>
                        </div>
                        <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                            I am summary
                            <div  style={{minWidth:'310px', height:'400px', maxWidth:'600px', margin:'0 auto'}}>
                                {/*<Chart/>*/}
                            </div>
                            {/*<Chart/>*/}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Chart extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {

    }

    render() {
        return Highcharts.chart('chart', {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: 'Browser market shares January, 2015 to May, 2015'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                name: 'Brands',
                colorByPoint: true,
                data: [{
                    name: 'Microsoft Internet Explorer',
                    y: 56.33
                }, {
                    name: 'Chrome',
                    y: 24.03,
                    sliced: true,
                    selected: true
                }, {
                    name: 'Firefox',
                    y: 10.38
                }, {
                    name: 'Safari',
                    y: 4.77
                }, {
                    name: 'Opera',
                    y: 0.91
                }, {
                    name: 'Proprietary or Undetectable',
                    y: 0.2
                }]
            }]
        });
    }
}