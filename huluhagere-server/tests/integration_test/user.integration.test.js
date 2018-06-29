process.env.NODE_ENV = 'test';

let mongoose = require("mongoose");
const User = require('../../models/user');

//Require the dev-dependencies
let chai = require('chai');
let chaiHttp = require('chai-http');
let server = require('../../app');
let should = chai.should();
let expect = chai.expect;

chai.use(chaiHttp);

describe("Site user test", () => {

	beforeEach((done) => { //Before each test we empty the database
        User.remove({}, (err) => { 
           done();         
        });     
    });

	afterEach((done) => {
		User.remove({}, (err) => {
			done();
		});
	});
});

describe('/GET /user/all', () => {
  it('It should return all users', (done) => {
  	let preference = {}
    chai.request(server)
        .get('/user/all')
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('array');
            res.body.length.should.be.eql(0);
          done();	
        });
 	});
});

describe('/GET /user/one', () => {
  it('It should return one users', (done) => {
    chai.request(server)
        .get('/user/one?email=mezi@gmail.com')
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(false);
          done();	
        });
 	});
});

describe('/POST /user/login', () => {
  it('It should not login user', (done) => {
  	let account = {
		"email":"root@gmail.com",
		"password":"root"
	};
    chai.request(server)
        .post('/user/login')
        .send(account)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(false);
          done();	
        });
 	});
});

describe('/POST /user/register', () => {
  it('It should register a user', (done) => {
  	let account = {
		"email":"guest@freeworld.com",
		"password":"guest"
	};
    chai.request(server)
        .post('/user/register')
        .send(account)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();	
        });
 	});
});


describe('/POST /user/remove-one', () => {
  it('It should remove a user', (done) => {
  	let account = {
		"email":"guest@freeworld.com"
	};
    chai.request(server)
        .post('/user/remove-one')
        .send(account)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('sucess');
            res.body.sucess.should.equal(true);
          done();	
        });
 	});
});

describe('/POST /user/update', () => {
  it('It should update a user', (done) => {
  	let account = {
		"old":{
			"email":"mezi@gmail.com"
		},
		"update":{
			"email":"mezzz@gmail.com"
		}
	};
    chai.request(server)
        .post('/user/update')
        .send(account)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(false);
          done();	
        });
 	});
});