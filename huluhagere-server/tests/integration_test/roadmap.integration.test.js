process.env.NODE_ENV = 'test';

let mongoose = require("mongoose");
const RoadmapManager = require('../../models/roadmapManager');

//Require the dev-dependencies
let chai = require('chai');
let chaiHttp = require('chai-http');
let server = require('../../app');
let should = chai.should();
let expect = chai.expect;

chai.use(chaiHttp);

describe("RoadmapManager model test", () => {

	beforeEach((done) => { //Before each test we empty the database
        RoadmapManager.remove({}, (err) => { 
           done();         
        });     
    });

	afterEach((done) => {
		RoadmapManager.remove({}, (err) => {
			done();
		});
	});
})

describe('/GET roadmap/all', () => {
  it('It should return roadmap', (done) => {
  	let preference = {}
    chai.request(server)
        .get('/roadmap/all?email=guest@freeworld.com')
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('array');
            res.body.length.should.not.be.eql(0);
          done();
        });
 	});
});

describe('/POST roadmap/save', () => {
  it('It should save a roadmap', (done) => {
  	let roadmap = { 
		    "activities": [
		        {
		            "site": {
		                "_id": "5b2407e9c878824384913afc",
		                "name": "gafat",
		                "avg_cost": 50,
		                "opening_hour": "2018-06-15T18:29:44.776Z",
		                "closing_hour": "2018-06-15T18:30:02.115Z",
		                "__v": 4,
		                "tags": [
		                    "history",
		                    "war",
		                    "heroism",
		                    "tradition"
		                ],
		                "work_days": [
		                    "2018-06-15T18:30:02.115Z",
		                    "2018-06-15T18:30:02.115Z",
		                    "2018-06-15T18:30:02.115Z",
		                    "2018-06-15T19:50:41.294Z"
		                ],
		                "site_services": [
		                    {
		                        "name": "food",
		                        "service_level": 3,
		                        "_id": "5b2407e9c878824384913afd",
		                        "category": [
		                            "hotel",
		                            "entertainment",
		                            "culture"
		                        ]
		                    }
		                ],
		                "artifacts": [
		                    {
		                        "name": "Abraraw",
		                        "tag": "Rifle",
		                        "_id": "5b2407e9c878824384913afe"
		                    }
		                ],
		                "address": {
		                    "street": "Arsema Road",
		                    "kebele": "01",
		                    "city": "Debre Tabor",
		                    "zone": "South Gondar",
		                    "region": "Amhara",
		                    "_id": "5b26a56a2fa60b38584cbc18"
		                },
		                "image_url": "http://localhost:8000/sites/default.png"
		            },
		            "startTime": "2018-06-16T23:16:10.592Z",
		            "endTime": "2018-06-16T23:16:10.592Z"
		        },
		        {
		            "site": {
		                "_id": "5b262549dada7a2c28aefd97",
		                "name": "wolenkisa",
		                "avg_cost": 35.5,
		                "opening_hour": "2018-06-15T18:29:44.776Z",
		                "closing_hour": "2018-06-15T18:30:02.115Z",
		                "__v": 0,
		                "tags": [
		                    "history",
		                    "war",
		                    "heroism",
		                    "tradition"
		                ],
		                "work_days": [
		                    "2018-06-15T18:30:02.115Z",
		                    "2018-06-15T18:30:02.115Z",
		                    "2018-06-15T18:30:02.115Z"
		                ],
		                "site_services": [
		                    {
		                        "name": "food",
		                        "service_level": 3,
		                        "_id": "5b262549dada7a2c28aefd98",
		                        "category": [
		                            "hotel",
		                            "entertainment",
		                            "culture"
		                        ]
		                    }
		                ],
		                "artifacts": [
		                    {
		                        "name": "Anything",
		                        "tag": "Rifle",
		                        "_id": "5b262549dada7a2c28aefd99"
		                    }
		                ],
		                "address": {
		                    "region": "amhara",
		                    "zone": "s/gondar",
		                    "kebele": "03",
		                    "street": "Arsema",
		                    "_id": "5b262549dada7a2c28aefd9a"
		                },
		                "image_url": "http://localhost:8000/sites/default.png"
		            },
		            "startTime": "2018-06-16T23:16:10.593Z",
		            "endTime": "2018-06-16T23:16:10.593Z"
		        }
		    ],
		    "email": "guest@freeworld.com"
		}
    chai.request(server)
        .post('/roadmap/save')
        .send(roadmap)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('success');
            res.body.success.should.equal(true);
          done();
        });
 	});
});

describe('/POST roadmap/remove', () => {
  it('It should remove a roadmap', (done) => {
  	let roadmap = {
			"name":"wolenkisa"
		};
    chai.request(server)
        .post('/roadmap/remove')
        .send(roadmap)
        .end((err, res) => {
        	res.should.have.status(200);
            res.body.should.be.a('object');
            res.body.should.have.property('sucess');
            res.body.sucess.should.equal(true);
          done();
        });
 	});
});


