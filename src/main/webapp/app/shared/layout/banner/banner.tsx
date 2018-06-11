import './banner.css';

import * as React from 'react';
import { MenuOnBanner } from './menu/menu-on-banner';
import { Col, Row } from 'reactstrap';

export class Banner extends React.Component {
  constructor(props) {
    super(props);
  }

  render () {
    return (
      <MenuOnBanner />
    );
  }
}
