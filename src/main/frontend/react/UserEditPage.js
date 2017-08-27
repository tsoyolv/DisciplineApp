'use strict';

const React = require('react');

export default class UserEditApp extends React.Component {

    render() {
        return  <div>
            <h1 className="page-header">Oleg Tsoi</h1>
            <div className="row">
                {/*left column*/}
                <div className="col-md-3">
                    <div className="text-center">
                        <img src="//placehold.it/100" className="avatar img-circle" alt="avatar"/>
                        <h6>Upload a photo...</h6>

                        <input type="file" className="form-control"/>
                    </div>
                </div>

                {/* edit form column*/}
                <div className="col-md-9 personal-info">
                    <div className="alert alert-info alert-dismissable">
                        <a className="panel-close close" data-dismiss="alert">×</a>
                        <i className="fa fa-coffee"></i>
                        This is an <strong>.alert</strong>. Use this to show important messages to the user.
                    </div>
                    <h3>Personal info</h3>
                    <form className="form-horizontal" role="form">
                        <div className="form-group">
                            <label className="col-lg-3 control-label">First name:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value="Jane"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Second name:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value="Bishop"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Last name:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value="Bishop"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Birth date:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="date"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">City:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value=""/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Country:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value=""/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Email:</label>
                            <div className="col-lg-8">
                                <input className="form-control" type="text" value="janesemail@gmail.com"/>
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="col-md-3 control-label">Username:</label>
                            <div className="col-md-8">
                                <input className="form-control" type="text" value="janeuser"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-md-3 control-label">Password:</label>
                            <div className="col-md-8">
                                <input className="form-control" type="password" value="11111122333"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-md-3 control-label">Confirm password:</label>
                            <div className="col-md-8">
                                <input className="form-control" type="password" value="11111122333"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-lg-3 control-label">Drop down example:</label>
                            <div className="col-lg-8">
                                <div className="ui-select">
                                    <select id="user_time_zone" className="form-control">
                                        <option value="Hawaii">(GMT-10:00) Hawaii</option>
                                        <option value="Pacific Time (US &amp; Canada)">(GMT-08:00) Pacific Time (US &amp; Canada)</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="col-md-3 control-label"></label>
                            <div className="col-md-8">
                                <input type="button" className="btn btn-primary" value="Save Changes"/>
                                <span></span>
                                <input type="reset" className="btn btn-default" value="Cancel"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>;
    }
}