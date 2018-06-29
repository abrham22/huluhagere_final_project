var sinon = require('sinon');
var chai = require('chai');
var expect = chai.expect;

var mongoose = require('mongoose');
require('sinon-mongoose');

//Importing our Prac model for our unit testing.
var Prac = require('../../models/prac');

/*
 * Unit test for savePrac
 */

describe("Save prac", function(){
    // Test will pass if we save prac
    it("should save a prac", function(done){
        var PracMock = sinon.mock(new Prac({
            "name": "horticulture" 
        }));
        var prac = PracMock.object
        var expectedResult = { success:true };
    	PracMock.expects('save').yields(null, expectedResult);
        prac.save(function (err, result) {
            PracMock.verify();
            PracMock.restore();
            expect(result.success).to.be.true;
            done();
        });	
        
    });

    // Test will pass if we fail to save prac
    it("should return error", function(done){
        var PracMock = sinon.mock(new Prac({
            "name": "horticulture" 
        }));
        var prac = PracMock.object
        var expectedResult = { success:false };
    	PracMock.expects('save').yields(expectedResult, null);
        prac.save(function (err, result) {
            PracMock.verify();
            PracMock.restore();
            expect(err.success).to.not.be.true;
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
        var PracMock = sinon.mock(Prac);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	PracMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Prac.findOne({name: "horticulture"}, function(err, result){
           	PracMock.verify();
	        PracMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.artifacts.push({ 
                name:"digger", 
                tag:"whatever"
            });
            PracMock = sinon.mock(new Prac(result));
            var prac = PracMock.object;
            expectedResult = { success:true };
    		PracMock.expects('save').yields(null, expectedResult); 
    		prac.save(function (err, result) {
		        PracMock.verify();
		        PracMock.restore();
		        expect(result.success).to.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail add artifact
    it("should return error: prac not found", function(done){
        var PracMock = sinon.mock(Prac);
        var expectedResult = {success:false, err:[]};
    	PracMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(expectedResult, null);
    	Prac.findOne({name: "horticulture"}, function(err, result){
           	PracMock.verify();
	        PracMock.restore();
	        expect(err.success).to.not.be.true;
	        done();
    	});	
    });

    // Test will pass if we fail to add artifact
    it("should return error at adding artifact", function(done){
        var PracMock = sinon.mock(Prac);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	PracMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Prac.findOne({name: "horticulture"}, function(err, result){
           	PracMock.verify();
	        PracMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result.artifacts.push({ 
                name:"digger", 
                tag:"whatever"
            });
            PracMock = sinon.mock(new Prac(result));
            var prac = PracMock.object;
            expectedResult = { success:false, err:[] };
    		PracMock.expects('save').yields(expectedResult, null); 
    		prac.save(function (err, result) {
		        PracMock.verify();
		        PracMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });

    // Test will pass if we fail to add artifact
    it("should return error: result prac is null", function(done){
        var PracMock = sinon.mock(Prac);
        var expectedResult = {name:"horticulture", artifacts:[]};
    	PracMock.expects('findOne')
    	.withArgs({name: "horticulture"})
    	.yields(null, expectedResult);
    	Prac.findOne({name: "horticulture"}, function(err, result){
           	PracMock.verify();
	        PracMock.restore();
	        expect(result.name).to.be.equal(expectedResult.name);
            result = null;
            PracMock = sinon.mock(new Prac(result));
            var prac = PracMock.object;
            expectedResult = { success:false, err:[] };
    		PracMock.expects('save').yields(expectedResult, null); 
    		prac.save(function (err, result) {
		        PracMock.verify();
		        PracMock.restore();
		        expect(err.success).to.not.be.true;
		        done();
		    });
    	});	
    });
});

/*
 * Unit test for findAll
 */

describe("Get all pracs", function(){
    // Test will pass if we get all pracs
    it("should return all pracs", function(done){
        var PracMock = sinon.mock(Prac);
        var prac = PracMock.object
        var expectedResult = { success:true, pracs:[] };
    	PracMock.expects('find')
    	.withArgs({})
    	.yields(null, expectedResult);
        prac.find({}, function (err, result) {
            PracMock.verify();
            PracMock.restore();
            expect(result.success).to.be.true;
            done();
        });	
        
    });

    // Test will pass if we fail to save prac
    it("should return error", function(done){
        var PracMock = sinon.mock(Prac);
        var prac = PracMock.object
        var expectedResult = { success:false, err:[] };
    	PracMock.expects('find')
    	.withArgs({})
    	.yields(expectedResult, null);
        prac.find({}, function (err, result) {
            PracMock.verify();
            PracMock.restore();
            expect(err.success).to.not.	be.true;
            done();
        });	
        
    });
});