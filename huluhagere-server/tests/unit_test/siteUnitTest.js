var sinon = require('sinon');
var chai = require('chai');
var expect = chai.expect;

var mongoose = require('mongoose');
require('sinon-mongoose');

//Importing our site model for our unit testing.
var Site = require('../../models/site').model;

/*
 * Unit test for save
 */
describe("Save site", function(){
    // Test will pass if we get save a site
    it("should save one site", function(done){
        var SiteMock = sinon.mock(new Site({
			    "name": "gafat",
			    "address": {
			    	"region": "amhara",
			        "zone": "s/gondar",
			        "city": "Debre Tabor",
			        "kebele": "03",   
			        "street": "Arsema"
			    },
			    "artifacts":  [
			    	{
			            "name": "Abraraw",
			            "tag": "Rifle"
			        }
			    ],
			    "site_services": [
			    	{
			            "name": "food",
			            "service_level": 3,
			            "category": ["hotel","entertainment", "culture"]
			        }
			    ],
			    "avg_cost": 35.50,
			    "opening_hour": "2018-06-15T18:29:44.776Z",
			    "closing_hour": "2018-06-15T18:30:02.115Z",
			    "work_days": [
			    	"2018-06-15T18:30:02.115Z", "2018-06-15T18:30:02.115Z", "2018-06-15T18:30:02.115Z"
			    ],
			    "tags": [
			    	"history", "war", "heroism", "tradition"
			    ]
			}));
        var site = SiteMock.object
        var expectedResult = {
                success: true,
                message: "successfully saved",
                error: ""
            };
    	SiteMock.expects('save').yields(null, expectedResult);
        site.save(function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(result.success).to.be.true;
            expect(result.error).to.be.equal(expectedResult.error);
            expect(result.message).to.be.equal(expectedResult.message);
            done();
        });	
        
    });

    // Test will pass if we fail save site
    it("should return error", function(done){
        var SiteMock = sinon.mock(new Site({
			    "name": "gafat",
			    "address": {
			    	"region": "amhara",
			        "zone": "s/gondar",
			        "city": "Debre Tabor",
			        "kebele": "03",   
			        "street": "Arsema"
			    },
			    "artifacts":  [
			    	{
			            "name": "Abraraw",
			            "tag": "Rifle"
			        }
			    ],
			    "site_services": [
			    	{
			            "name": "food",
			            "service_level": 3,
			            "category": ["hotel","entertainment", "culture"]
			        }
			    ],
			    "avg_cost": 35.50,
			    "opening_hour": "2018-06-15T18:29:44.776Z",
			    "closing_hour": "2018-06-15T18:30:02.115Z",
			    "work_days": [
			    	"2018-06-15T18:30:02.115Z", "2018-06-15T18:30:02.115Z", "2018-06-15T18:30:02.115Z"
			    ],
			    "tags": [
			    	"history", "war", "heroism", "tradition"
			    ]
			}));
        var site = SiteMock.object
        var expectedResult = {
                success: false,
                message: "not saved",
                error: []
            };
    	SiteMock.expects('save').yields(expectedResult, null);
        site.save(function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(err.success).to.not.be.true;
            expect(err.error).to.be.equal(expectedResult.error);
            expect(err.message).to.be.equal(expectedResult.message);
            done();
        });	
        
    });
    
});

/*
 * Unit testing for findAll
 */

describe("Get all Sites", function(){
     // Test will pass if we get all sites
    it("should return all sites", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {sites:[]};
        SiteMock.expects('find')
        .withArgs({})
        .yields(null, expectedResult);
        Site.find({}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(result).to.be.equal(expectedResult);
            done();
        });	
    });

    // Test will pass if we fail to get a Site
    it("should return error", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: false,
                err: [],
                message: "Sites could not be fetched"
            };
        SiteMock.expects('find')
        .withArgs({})
        .yields(expectedResult, null);
        Site.find({}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(err.success).to.not.be.true;
            expect(err.err).to.be.equal(expectedResult.err);
            expect(err.message).to.be.equal(expectedResult.message);
            done();
        });
    });
});

/*
 * Unit testing for findOne
 */

describe("Get one Site", function(){
     // Test will pass if we get one site
    it("should return one site", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {site:{name:"siteName"}};
        SiteMock.expects('findOne')
        .withArgs({name:"siteName"})
        .yields(null, expectedResult);
        Site.findOne({name:"siteName"}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(result).to.be.equal(expectedResult);
            done();
        });	
    });

    // Test will pass if we fail to get a Site
    it("should return error", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: false,
                err: [],
                message: "not found"
            };
        SiteMock.expects('findOne')
        .withArgs({name:"siteName"})
        .yields(expectedResult, null);
        Site.findOne({name:"siteName"}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(err.success).to.not.be.true;
            expect(err.err).to.be.equal(expectedResult.err);
            expect(err.message).to.be.equal(expectedResult.message);
            done();
        });
    });
});

/*
 * Unit test for addArtifact
 */

describe("Add artifact", function(){
    // Test will pass if we add artifact
    it("should add an artifact", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.artifacts.push({ 
                name:"digger", 
                tag:"whatever"
            });
            SiteMock = sinon.mock(new Site(result));
            var site = SiteMock.object;
            expectedResult = { success:true };
    		SiteMock.expects('save').yields(null, expectedResult); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(result.success).to.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail add artifact
    it("should return error: Site not found", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {success:false, err:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(expectedResult, null);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(err.success).to.not.be.true;
	        done();
    	});	
    });

    // Test will pass if we fail to add artifact
    it("should return error at adding artifact", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.artifacts.push({ 
                name:"digger", 
                tag:"whatever"
            });
            SiteMock = sinon.mock(new Site(result));
            var site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail to add artifact
    it("should return error: result Site is null", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result = null;
            SiteMock = sinon.mock(new Site(result));
            site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });
});

/*
 * Unit test for addService
 */

describe("Add service", function(){
    // Test will pass if we add service
    it("should add a service", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", site_services:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.site_services.push({
					"name":"tela",
					"category":["beverage","party","feast"],
					"service_level":4
				});
            SiteMock = sinon.mock(new Site(result));
            site = SiteMock.object;
            expectedResult = { success:true };
    		SiteMock.expects('save').yields(null, expectedResult); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(result.success).to.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail add service
    it("should return error: Site not found", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {success:false, err:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(expectedResult, null);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(err.success).to.not.be.true;
	        done();
    	});	
    });

    // Test will pass if we fail to add service
    it("should return error at adding service", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", site_services:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.site_services.push({
					"name":"tela",
					"category":["beverage","party","feast"],
					"service_level":4
				});
            SiteMock = sinon.mock(new Site(result));
            var site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail to add artifact
    it("should return error: result Site is null", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", site_services:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result = null;
            SiteMock = sinon.mock(new Site(result));
            site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });
});

/*
 * Unit test for addWorkingDay
 */

describe("Add working day", function(){
    // Test will pass if we add working day
    it("should add a working day", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", work_days:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.work_days.push({
					"name":"gafat",
					"day": "2018-06-15T19:50:41.294Z"
				});
            SiteMock = sinon.mock(new Site(result));
            site = SiteMock.object;
            expectedResult = { success:true };
    		SiteMock.expects('save').yields(null, expectedResult); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(result.success).to.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail add working day
    it("should return error: Site not found", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {success:false, err:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(expectedResult, null);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(err.success).to.not.be.true;
	        done();
    	});	
    });

    // Test will pass if we fail to add working day
    it("should return error at adding working day", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", work_days:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.work_days.push({
					"name":"gafat",
					"day": "2018-06-15T19:50:41.294Z"
				});
            SiteMock = sinon.mock(new Site(result));
            var site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail to add working day
    it("should return error: result Site is null", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {name:"horticulture", work_dayso:[]};
    	SiteMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Site.findOne({name: "horticulture"}, function(err, result){
           	SiteMock.verify();
	        SiteMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result = null;
            SiteMock = sinon.mock(new Site(result));
            site = SiteMock.object;
            expectedResult = { success:false, err:[] };
    		SiteMock.expects('save').yields(expectedResult, null); 
    		site.save(function (err, result) {
		        SiteMock.verify();
		        SiteMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });
});

/*
 * Unit test for update
 */
describe("Update a site", function(){
	var site_name = {"name":"gafat"};
        var update = {"update": {
					"avg_cost":"50.00",
					"address": {
			            "region": "Amhara",
			            "zone": "South Gondar",
			            "city": "Debre Tabor",
			            "kebele": "01",
			            "street": "Arsema Road"
			        }
				}};
        var expectedResult = {
                success: true,
                message: "successfully updated",
                error: "'"
            };
     // Test will pass if we update a site
    it("should update a site", function(done){
        var SiteMock = sinon.mock(Site);
        SiteMock.expects('findOneAndUpdate')
        .withArgs(site_name, update)
        .yields(null, expectedResult);
        Site.findOneAndUpdate(site_name, update, function (err, result) {
                SiteMock.verify();
                SiteMock.restore();
                expect(result.success).to.be.true;
                expect(result.Site).to.be.equal(expectedResult.Site);
                expect(result.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });

    // Test will pass if we fail to get a Site
    it("should return error", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: false,
                message: "not updated",
                error: []
            };
        SiteMock.expects('findOneAndUpdate')
        .withArgs(site_name, update)
        .yields(expectedResult, null);
        Site.findOneAndUpdate( site_name, update, function (err, result) {
                SiteMock.verify();
                SiteMock.restore();
                expect(err.success).to.not.be.true;
                expect(err.Site).to.be.equal(expectedResult.Site);
                expect(err.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });
});

/*
 * Unit test for remove
 */
describe("Remove one site", function(){
     // Test will pass if we remove one site
    it("should remove one site", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: true,
                message: "successfully removed",
                error: "'"
            };
        SiteMock.expects('remove')
        .withArgs({name:'siteName'})
        .yields(null, expectedResult);
        Site.remove({name:'siteName'}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(result.success).to.be.true;
            expect(result.message).to.be.equal(expectedResult.message);
            done();
        });	
    });

    // Test will pass if we fail to get a Site
    it("should return error", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: false,
                message: "not removed",
                error: []
            };
        SiteMock.expects('remove')
        .withArgs({name:'siteName'})
        .yields(expectedResult, null);
        Site.remove({name:'siteName'}, function (err, result) {
            SiteMock.verify();
            SiteMock.restore();
            expect(err.success).to.not.be.true;
            expect(err.message).to.be.equal(expectedResult.message);
            done();
        });
    });
});