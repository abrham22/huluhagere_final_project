process.env.NODE_ENV = 'test';

let mongoose = require("mongoose");
let  Site = require('../../models/site').model;

//Require the dev-dependencies
let chai = require('chai');
let chaiHttp = require('chai-http');
let server = require('../../app');
let should = chai.should();
let expect = chai.expect;

chai.use(chaiHttp);

describe("Site model test", () => {

	beforeEach((done) => { //Before each test we empty the database
        Site.remove({}, (err) => { 
           done();         
        });     
    });

	afterEach((done) => {
		Site.remove({}, (err) => {
			done();
		});
	});
})

describe('/POST generate roadmap', () => {
  it('It should generate roadmap', (done) => {
  	let preference = {"preference":{
		    "services": [
		        {
		            "name": "food",
		            "service_level": 3,
		            "category": [
		                "hotel"
		            ]
		        }
		    ],
		    "artifacts": [
		        {
		            "name": "Abraraw",
		            "tag": "Rifle"
		        }
		    ],
		    "locations": [{
		        "city": "Debre Tabor"
		    },
		    {
		        "city": "Mekele"
		    }],
		    "tags": ["history", "tradition", "culture"],
		    "duration": 45.6
		}}
    chai.request(server)
        .post('/generator/roadmap')
        .send(preference)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('preference');
            res.body.preference.should.have.property('services');
          done();
        });
 	});
});
