/* DANGER! There are some limits for config. You should add all outputs in built folder */
var path = require('path');

var node_dir = __dirname + '/node_modules';

module.exports = {
    entry: './src/main/frontend/react/app.js',
    devtool: 'sourcemaps',
    cache: true,
    debug: true,
    resolve: {
        alias: {
            'stompjs': node_dir + '/stompjs/lib/stomp.js',
        }
    },
    output: {
        path: __dirname,
        filename: './src/main/frontend/webresources/templates/built/bundle.js'
    },
    module: {
        loaders: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015', 'react']
                }
            },
            { test: /\.css$/, loader: "style-loader!css-loader" }
        ],
        resolve: {
            extensions: ['', '.js', '.jsx', '.css'],
            modulesDirectories: [
                node_dir
            ]
        }
    }
};