process.env.NODE_ENV = 'test';

let mongoose = require("mongoose");
const Site = require('../../models/site');

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
});

describe('/GET /site/all', () => {
  it('It should return all site', (done) => {
  	let preference = {}
    chai.request(server)
        .get('/site/all')
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('array');
            res.body.length.should.not.be.eql(0);
          done();
        });
 	});
});

describe('/GET /site/one', () => {
  it('It should return one site', (done) => {
  	let preference = {}
    chai.request(server)
        .get('/site/one?name=gafat')
        .end((err, res) => {
        	res.should.have.status(200);
          done();
        });
 	});
});

describe('/POST /site/save', () => {
  it('It should save a site', (done) => {
  	let site = {
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
	            "name": "Anything",
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
	};

    chai.request(server)
        .post('/site/save')
        .send(site)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

describe('/POST /site/add-service', () => {
  it('It should add service', (done) => {
  	let artifact = {
		"name":"gafat",
		"service": {
			"name":"tela",
			"category":["beverage","party","feast"],
			"service_level":4
		}
	};

    chai.request(server)
        .post('/site/add-service')
        .send(artifact)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

describe('/POST /site/add-working-day', () => {
  it('It should add workgin-day', (done) => {
  	let artifact = {
		"name":"gafat",
		"day": "2018-06-15T19:50:41.294Z"
	};

    chai.request(server)
        .post('/site/add-working-day')
        .send(artifact)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

describe('/POST /site/update', () => {
  it('It should update site', (done) => {
  	let site = {
		"name":"gafat",
		"update": {
			"avg_cost":"50.00",
			"address": {
	            "region": "Amhara",
	            "zone": "South Gondar",
	            "city": "Debre Tabor",
	            "kebele": "01",
	            "street": "Arsema Road"
	        }
		}
	};

    chai.request(server)
        .post('/site/update')
        .send(site)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

describe('/POST /site/remove', () => {
  it('It should update site', (done) => {
  	let site = {
		"name":"wolenkisa"
	};

    chai.request(server)
        .post('/site/remove')
        .send(site)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

