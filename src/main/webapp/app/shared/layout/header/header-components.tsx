import * as React from 'react';
import { Translate } from 'react-jhipster';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu, NavItem, NavLink, NavbarBrand, Breadcrumb, BreadcrumbItem } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id}>
    <DropdownToggle nav caret className="d-flex align-items-center">
      <FontAwesomeIcon icon={props.icon} />
      <span>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="static/images/logo-jhipster-react.svg" alt="Logo" />
  </div>
);

export const BreadcrumbComponent = props => (
  <Breadcrumb>
  { props.layout.map((item) =>
    (item.active ?
      (<BreadcrumbItem active>{ item.label }</BreadcrumbItem>)
      :
      (<BreadcrumbItem><a href={ item.url }>{ item.label }</a></BreadcrumbItem>)
    ) )
  }
  </Breadcrumb>
);

export const Brand = props => (
  <Breadcrumb>
    <BreadcrumbItem><a href="#">All Page</a></BreadcrumbItem>
    <BreadcrumbItem active>Library</BreadcrumbItem>
  </Breadcrumb>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);
