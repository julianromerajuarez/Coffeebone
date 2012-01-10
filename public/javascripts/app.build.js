({
    appDir: "../",
    baseUrl: "javascripts/app",
    dir: "../../public-build",
    optimize: "uglify",
    pragmasOnSave: {
        //Just an example
        excludeCoffeeScript: true
    },
    
    paths: {
		cs:			'../lib/cs-0.3.1',
        text:		'../lib/text-1.0.2',
		jquery:		'../lib/jquery-1.6.4',
		underscore:	'../lib/underscore-1.2.3',
		backbone:	'../lib/backbone-0.5.3-amd'
	},

	modules: [
        {
            name: "../main"
        }
    ]
})
