import * as React from 'react';
import { DropdownItem } from 'reactstrap';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/region">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Region
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/city">
      <FontAwesomeIcon icon="asterisk" />&nbsp; City
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/district">
      <FontAwesomeIcon icon="asterisk" />&nbsp; District
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/street">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Street
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-region">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Region
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/house">
      <FontAwesomeIcon icon="asterisk" />&nbsp; House
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/service-fee">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Service Fee
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/house-tracking">
      <FontAwesomeIcon icon="asterisk" />&nbsp; House Tracking
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/house-photo">
      <FontAwesomeIcon icon="asterisk" />&nbsp; House Photo
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/land-projects">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Land Projects
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/article">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Article
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/category">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Category
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-profile">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Profile
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-subscription">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Subscription
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-tracking">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Tracking
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-feed">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Feed
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/search-tracking">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Search Tracking
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-financial">
      <FontAwesomeIcon icon="asterisk" />&nbsp; User Financial
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/potential-customer">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Potential Customer
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/banner">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Banner
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/payment">
      <FontAwesomeIcon icon="asterisk" />&nbsp; Payment
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
