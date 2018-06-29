var sinon = require('sinon');
var chai = require('chai');
var expect = chai.expect;

var mongoose = require('mongoose');
require('sinon-mongoose');

//Importing our Site model for our unit testing.
var Site = require('../../models/site').model;

/*
 * Unit testing for generate
 */

describe("Generate roadmap", function(){
     // Test will pass if we get all todos
    // it("should return a roadmap", function(done){
    //     var SiteMock = sinon.mock(Site);
    //     var expectedResult = [];
    //     SiteMock.expects('find')
    //     .withArgs({})
    //     .yields(null, expectedResult);
    //     Site.find({}, function (err, result) {
    //         SiteMock.verify();
    //         SiteMock.restore();
    //         done();
    //     });	
    // });

    // Test will pass if we fail to get a Site
    it("should return error", function(done){
        var SiteMock = sinon.mock(Site);
        var expectedResult = {
                success: false,
                err: [],
                "message": "Roadmap could not be generated"
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