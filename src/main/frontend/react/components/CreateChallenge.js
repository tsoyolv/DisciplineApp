'use strict';

const React = require('react');
const client = require('./../modules/client');
const ReactDOM = require('react-dom');


export default class CreateChallenge extends React.Component {
    constructor(props) {
        super(props);
        this.state = {attrs: []};
        this.onCreate = this.onCreate.bind(this);
    }

    componentDidMount() {
        client({
            method: 'GET',
            path: 'api/profile/challenges',
            headers: {
                'Accept': 'application/schema+json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).then(schema => {
            /**
             * Filter unneeded JSON Schema properties, like uri references and
             * subtypes ($ref).
             */
            Object.keys(schema.entity.properties).forEach(function (property) {
                if (schema.entity.properties[property].hasOwnProperty('format') &&
                    schema.entity.properties[property].format === 'uri') {
                    delete schema.entity.properties[property];
                }
                else if (schema.entity.properties[property].hasOwnProperty('$ref')) {
                    delete schema.entity.properties[property];
                }
            });

            this.schema = schema.entity;
            this.setState({attrs:Object.keys(this.schema.properties)});
        });
    }

    onCreate(newChallenge) {
        client({
            method: 'POST',
            path: 'api/challenges',
            entity: newChallenge,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")
            }
        }).done(response => {
            /*if (response.status.code === 201) {
             this.setState({alert:{entity:response.entity, message:'Created successfully! Created habit: '}})
             }*/
            /* Let the websocket handler create the state */
        }, response => {
            if (response.status.code === 500) {
                alert('Required fields are not filled. Creation is failed.');
            }
        });
    }

    render () {
        var filteredAttrs = this.state.attrs;
        return (
            <div>
                <a href="#createChallenge" data-toggle="modal" data-target="#createChallenge">Create</a>
                <CreateDialog attributes={filteredAttrs} onCreate={this.onCreate} modalId="createChallenge" titleName="Create challenge" buttonName="Create"/>
            </div>
        );
    }
}

class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.filterAttrs();
    }

    handleSubmit(e) {
        e.preventDefault();
        var filterAttrs = this.filterAttrs();
        var object = {};
        filterAttrs.forEach(attribute => {
            object[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(object);

        // clear out the dialog's inputs
        filterAttrs.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        $('#' + this.props.modalId).modal('hide');
        window.location = "#"; // todo close modal dialog
    }

    filterAttrs() {
        return this.props.attributes.filter(attr =>
        attr != 'completed' &&
        attr != 'completedCount' &&
        attr != 'createdWhen' &&
        attr != 'acceptedCount' &&
        attr != 'withCreator' &&
        attr != 'votes' &&
        attr != 'forAllUsers' &&
        attr != 'challengeDate' &&
        attr != 'updatedWhen');
    }

    render() {
        var inputs = this.filterAttrs()
            .map(attribute =>
                <p key={attribute}>
                    <input type="text" placeholder={attribute} ref={attribute} className="field"/>
                </p>
            );

        return (
            <div className="modal fade" id={this.props.modalId} tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">{this.props.titleName}</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <form>
                            <div className="modal-body">
                                <p key="name">
                                    <label className="col-lg-3 control-label">Name:</label>
                                    <div className="col-lg-8">
                                        <input type="text" placeholder="" ref="name" className="field"/>
                                    </div>
                                </p>
                                <p key="difficulty">
                                    <label className="col-lg-3 control-label">Difficulty:</label>
                                    <div className="col-lg-8">
                                        <input type="text" placeholder="" ref="difficulty" className="field"/>
                                    </div>
                                </p>
                                <p key="description">
                                    <label className="col-lg-3 control-label">Description:</label>
                                    <div className="col-lg-8">
                                        <input type="text" placeholder="" ref="description" className="field"/>
                                    </div>
                                </p>
                                <p key="type">
                                    <label className="col-lg-3 control-label">Type:</label>
                                    <div className="col-lg-8">
                                        <div className="ui-select">
                                            <select id="type" className="form-control" ref="type">
                                                <option value="HABIT">Habit</option>
                                                <option value="NORMAL">Normal</option>
                                            </select>
                                        </div>
                                    </div>
                                </p>
                                <p key="sphere">
                                    <label className="col-lg-3 control-label">Sphere:</label>
                                    <div className="col-lg-8">
                                        <div className="ui-select">
                                            <select id="sphere" className="form-control" ref="sphere">
                                                <option value="HEALTH">Health</option>
                                                <option value="INTELLECT">Intellect</option>
                                                <option value="MENTALITY">Mentality</option>
                                                <option value="FAMILY">Family</option>
                                            </select>
                                        </div>
                                    </div>
                                </p>
                            </div>

                            <div className="modal-footer">
                                <button type="button" className="btn btn-primary" onClick={this.handleSubmit}>{this.props.buttonName}</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}