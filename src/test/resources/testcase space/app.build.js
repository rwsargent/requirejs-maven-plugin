({
	appDir: './',
    baseUrl: "./js",
    dir: "../output/spaces/1",
    skipDirOptimize: true,
    keepBuildDir : true,
    cacheFile :  "./cache.sm",
    modules : [
        {
            name : 'accessioning/accessioning_module'
        },
        {
            name : 'accessioning2.0/accessioning_module'
        },
        {
            name : 'dynamic_reports/actionable_dynamic_report_module'
        },
        {
            name : 'dynamic_reports/actionable_plus_dialog_module'
        },
        {
            name : 'adhoc_item/adhoc_module'
        },
        {
            name : 'agent_monitor/agent_monitor_module'
        },
        {
            name : 'barcode_printing/barcode_printing_module'
        },
        {
            name : 'build_tower/build_tower_module'
        },
        {
            name : 'bulk_problem_reporting/bulk_problem_reporting_module'
        },
        {
            name : 'dna_capture/dna_capture_module'
        },
        {
            name : 'download_agents/download_agents_module'
        },
        {
            name : 'dynamic_reports/dynamic_reports_module'
        },
        {
            name : 'dynamic_reports/dynamic_reports_printable_module'
        },
        {
            name : 'gizmos/gizmo_testbed_module'
        },
        {
            name : 'hemoglobin/hemoglobin_module'
        },
        {
            name : 'login/login_module'
        },
        {
            name : 'master_runner/master_runner_module'
        },
        {
            name : 'metric_reports/metric_report_module'
        },
        {
            name : 'metrics/metrics_module'
        },
        {
            name : 'molecular_test/molecular_test_module'
        },
        {
            name : 'navigation/navigation_module'
        },
        {
            name : 'printqueue/print_queue_module'
        },
        {
            name : 'problem_records/problem_records_module'
        },
        {
            name : 'racking2.0/racking_module'
        },
        {
            name : 'reagent_prep/bead_prep_module'
        },
        {
            name : 'specimen_processing/specimen_processing_module'
        },
        {
            name : 'storage/storage_module'
        },
        {
            name : 'thething/thething_module'
        },
        {
            name : 'workstation_manager/workstation_manager_module'
        },
        {
            name : 'workstation_settings/workstation_settings_module'
        }
    ],
    paths : {
        jquery : 'libs/jquery-2.0.2',
        jqueryui : 'libs/jquery-ui-1.10.3/ui/jquery-ui',
        mockjax : 'libs/jquery.mockjax',
        underscore : 'libs/underscore-1.4.4',
        backbone : 'libs/backbone-1.0.0',
        text : 'libs/text',
        json_schema_version : 'libs/jsv-793c171/json-schema-draft-03',
        jsv : 'libs/jsv-793c171/jsv',
        ladda : "libs/ladda",
        jqueryRotate : "libs/jQueryRotate.2.2",
        jquerymaskedinput : 'libs/jquery.mask',
        perfectScrollbar : 'libs/perfect-scrollbar-0.5.1/src/perfect-scrollbar',
        velocity : 'libs/velocity.min',
        velocityui: 'libs/velocity.ui',
        d3: 'libs/d3.min',
        nvd3: 'libs/nvd3.min',
        codemirror: 'libs/codemirror/lib/codemirror',
        datetimepicker : 'libs/datetimepicker-master/jquery.datetimepicker',
        clipboard : 'libs/clipboard.min',
        typeahead : 'libs/typeaheadjs/typeahead.jquery.min',
        bloodhound : 'libs/typeaheadjs/bloodhound'
    },
    shim : {
    	jqueryui : {
    		deps: ["jquery"]
    	},
        underscore : {
            exports : '_'
        },
        backbone : {
            deps : [
                "underscore",
                "jquery"
            ],
            exports : "Backbone"
        },
        jqueryRotate : {
            deps : [
                "jquery",
                "jqueryui"
            ]
        },
        velocity: {
            deps:["jquery"]
        },
        velocityui: {
            deps:["velocity"]
        },
        datetimepicker: {
        	deps:["jquery"]
        },
        d3: {
            exports: 'd3'
        },
        nvd3: {
            exports: 'nv',
            deps:["d3"]
        },
        typeahead: {
        	deps:["jquery"]
        }, 
        bloodhound : {
        	deps:["jquery"]
        }
    }
})
