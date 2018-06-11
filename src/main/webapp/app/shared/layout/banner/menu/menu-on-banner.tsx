import './menu-on-banner.css';

import * as React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row, Button } from 'reactstrap';

export const MenuOnBanner = props => (
  <Row>
    <Col md="12">
      <ul className="menu-on-banner">
        <li className="on-left">
          <a>
            <span className="first">Land</span>
            <span className="second">Exp</span>
          </a>
          <a>Buy</a>
          <a>Hire</a>
          <a>Support</a>
          <a>News</a>
        </li>
        <li className="on-right">
          <a>Login</a>
          <a>Register</a>
          <Button color="warning">Post News</Button>
        </li>
      </ul>
    </Col>
  </Row>
);
