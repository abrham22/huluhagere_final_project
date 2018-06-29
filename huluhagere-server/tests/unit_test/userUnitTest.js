var sinon = require('sinon');
var chai = require('chai');
var expect = chai.expect;

var mongoose = require('mongoose');
require('sinon-mongoose');

//Importing our user model for our unit testing.
var User = require('../../models/user').model;

/*
 * Unit testing for getAllUsers
 */

describe("Get all users", function(){
     // Test will pass if we get all todos
    it("should return all users", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = [];
        UserMock.expects('find')
        .withArgs({})
        .yields(null, expectedResult);
        User.find({}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            done();
        });	
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = [];
        UserMock.expects('find')
        .withArgs({})
        .yields(expectedResult, null);
        User.find({}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(err).to.not.be.true;
            done();
        });
    });
});

/*
 * Unit test for searchUser
 */
describe("Get one user", function(){
     // Test will pass if we get all todos
    it("should return one user", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success:true, user:{}};
        UserMock.expects('findOne')
        .withArgs({email:'email@email.com'})
        .yields(null, expectedResult);
        User.findOne({email:'email@email.com'}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(result.success).to.be.true;
            done();
        });	
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success: false, error: []};
        UserMock.expects('findOne')
        .withArgs({email:'email@email.com'})
        .yields(expectedResult, null);
        User.findOne({email:'email@email.com'}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(err.success).to.not.be.true;
            done();
        });
    });
});

/*
 * Unit test for removeAllUsers
 */
describe("Remove all user", function(){
     // Test will pass if we get all todos
    it("should remove all user", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success:true, message: "All users successfully deleted"};
        UserMock.expects('remove')	
        .yields(null, expectedResult);
        User.remove(function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(result.success).to.be.true;
            expect(result.message).to.be.equal(
            	"All users successfully deleted");
            done();
        });	
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success: false, error: []};
        UserMock.expects('remove')
        .yields(expectedResult, null);
        User.remove(function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(err.success).to.not.be.true;
            done();
        });
    });
});

/*
 * Unit test for removeUsers
 */
describe("Remove one user", function(){
     // Test will pass if we get all todos
    it("should remove one user", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success:true, message: "User successfully deleted"};
        UserMock.expects('findOneAndRemove')
        .withArgs({email:'email@email.com'})
        .yields(null, expectedResult);
        User.findOneAndRemove({email:'email@email.com'}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(result.success).to.be.true;
            expect(result.message).to.be.equal(
            	"User successfully deleted");
            done();
        });	
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {success: false, error: []};
        UserMock.expects('findOneAndRemove')
        .withArgs({email:'email@email.com'})
        .yields(expectedResult, null);
        User.findOneAndRemove({email:'email@email.com'}, function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(err.success).to.not.be.true;
            done();
        });
    });
});

/*
 * Unit test for registerUser
 */
describe("Register user", function(){
    // Test will pass if we get all todos
    it("should register one user", function(done){
        var UserMock = sinon.mock(new User({
            "email":"email@gmail.com2",
            "password":"pass"
        }));
        var user = UserMock.object
        var expectedResult = {
        	success:true,
        	error: "", 
        	message: 'You are successfully registered!'
        };
    	UserMock.expects('save').yields(null, expectedResult);
        user.save(function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(result.success).to.be.true;
            expect(result.error).to.be.equal(expectedResult.error);
            expect(result.message).to.be.equal(expectedResult.message);
            done();
        });	
        
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(new User({
            "email":"email@gmail.com2",
            "password":"pass"
        }));
        var user = UserMock.object
        var expectedResult = {
            success:false,
            error: [], 
            message: 'Email already exists!'
        };
        UserMock.expects('save').yields(expectedResult, null);
        user.save(function (err, result) {
            UserMock.verify();
            UserMock.restore();
            expect(err.success).to.not.be.true;
            expect(err.error).to.be.equal(expectedResult.error);
            expect(err.message).to.be.equal(expectedResult.message);
            done();
        }); 
    });
});

/*
 * Unit test for updateUser
 */
describe("Update a user", function(){
     // Test will pass if we update a user
    it("should update a user", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {
                success: true,
                message: 'User successfully updated!',
                user: []
            };
        UserMock.expects('findOneAndUpdate')
        .withArgs(
            {"email":'old@email.com'}, 
            {"email":'update@email.com'})
        .yields(null, expectedResult);
        User.findOneAndUpdate( 
            {"email":'old@email.com'}, 
            {"email":'update@email.com'},
            function (err, result) {
                UserMock.verify();
                UserMock.restore();
                expect(result.success).to.be.true;
                expect(result.user).to.be.equal(expectedResult.user);
                expect(result.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });

    // Test will pass if we fail to get a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {
                success: false,
                error: [],
                message: 'Update failed!'
            };
        UserMock.expects('findOneAndUpdate')
        .withArgs(
            {"email":'old@email.com'}, 
            {"email":'update@email.com'})
        .yields(expectedResult, null);
        User.findOneAndUpdate( 
            {"email":'old@email.com'}, 
            {"email":'update@email.com'},
            function (err, result) {
                UserMock.verify();
                UserMock.restore();
                expect(err.success).to.not.be.true;
                expect(err.user).to.be.equal(expectedResult.user);
                expect(err.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });
});

/*
 * Unit test for findUser
 */
describe("Update a user", function(){
     // Test will pass if we login a user
    it("should find and login a user", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {
                success: true,
                message: "You are successfully logged in!",
                error: ""
            };
        UserMock.expects('findOne')
        .withArgs({"email":'email@email.com'})
        .yields(null, expectedResult);
        User.findOne({"email":'email@email.com'}, function (err, result) {
                UserMock.verify();
                UserMock.restore();
                expect(result.success).to.be.true;
                expect(result.error).to.be.equal(expectedResult.error);
                expect(result.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });

    // Test will pass if we fail to login a user
    it("should return error", function(done){
        var UserMock = sinon.mock(User);
        var expectedResult = {
                success: false,
                error: [],
                message: 'Invalid email and password!'
            };
        UserMock.expects('findOne')
        .withArgs({"email":'email@email.com'})
        .yields(expectedResult, null);
        User.findOne({"email":'email@email.com'}, function (err, result) {
                UserMock.verify();
                UserMock.restore();
                expect(err.success).to.not.be.true;
                expect(err.error).to.be.equal(expectedResult.error);
                expect(err.message).to.be.equal(expectedResult.message);
                done();
            }
        ); 
    });
});