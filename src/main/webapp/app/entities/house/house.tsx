import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudSearchAction,
  ICrudGetAllAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './house.reducer';
import { IHouse } from 'app/shared/model/house.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IHouseProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IHouseState extends IPaginationBaseState {
  search: string;
}

export class House extends React.Component<IHouseProps, IHouseState> {
  state: IHouseState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { houseList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="house-heading">
          <Translate contentKey="landexpApp.house.home.title">Houses</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="landexpApp.house.home.createLabel">Create new House</Translate>
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('landexpApp.house.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('avatar')}>
                  <Translate contentKey="landexpApp.house.avatar">Avatar</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('actionType')}>
                  <Translate contentKey="landexpApp.house.actionType">Action Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('address')}>
                  <Translate contentKey="landexpApp.house.address">Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('money')}>
                  <Translate contentKey="landexpApp.house.money">Money</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('moneyType')}>
                  <Translate contentKey="landexpApp.house.moneyType">Money Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('acreage')}>
                  <Translate contentKey="landexpApp.house.acreage">Acreage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('discount')}>
                  <Translate contentKey="landexpApp.house.discount">Discount</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('direction')}>
                  <Translate contentKey="landexpApp.house.direction">Direction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('directionBalcony')}>
                  <Translate contentKey="landexpApp.house.directionBalcony">Direction Balcony</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('floor')}>
                  <Translate contentKey="landexpApp.house.floor">Floor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('numberOfFloor')}>
                  <Translate contentKey="landexpApp.house.numberOfFloor">Number Of Floor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('bathRoom')}>
                  <Translate contentKey="landexpApp.house.bathRoom">Bath Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('bedRoom')}>
                  <Translate contentKey="landexpApp.house.bedRoom">Bed Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('parking')}>
                  <Translate contentKey="landexpApp.house.parking">Parking</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('furniture')}>
                  <Translate contentKey="landexpApp.house.furniture">Furniture</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('landType')}>
                  <Translate contentKey="landexpApp.house.landType">Land Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('saleType')}>
                  <Translate contentKey="landexpApp.house.saleType">Sale Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('fee')}>
                  <Translate contentKey="landexpApp.house.fee">Fee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('feeMax')}>
                  <Translate contentKey="landexpApp.house.feeMax">Fee Max</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('present')}>
                  <Translate contentKey="landexpApp.house.present">Present</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('hits')}>
                  <Translate contentKey="landexpApp.house.hits">Hits</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('statusType')}>
                  <Translate contentKey="landexpApp.house.statusType">Status Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('createAt')}>
                  <Translate contentKey="landexpApp.house.createAt">Create At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('updateAt')}>
                  <Translate contentKey="landexpApp.house.updateAt">Update At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.district">District</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.city">City</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.street">Street</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.project">Project</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.createBy">Create By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.house.updateBy">Update By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {houseList.map((house, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${house.id}`} color="link" size="sm">
                      {house.id}
                    </Button>
                  </td>
                  <td>{house.avatar}</td>
                  <td>{house.actionType}</td>
                  <td>{house.address}</td>
                  <td>{house.money}</td>
                  <td>{house.moneyType}</td>
                  <td>{house.acreage}</td>
                  <td>{house.discount}</td>
                  <td>{house.direction}</td>
                  <td>{house.directionBalcony}</td>
                  <td>{house.floor}</td>
                  <td>{house.numberOfFloor}</td>
                  <td>{house.bathRoom}</td>
                  <td>{house.bedRoom}</td>
                  <td>{house.parking ? 'true' : 'false'}</td>
                  <td>{house.furniture ? 'true' : 'false'}</td>
                  <td>{house.landType}</td>
                  <td>{house.saleType}</td>
                  <td>{house.fee}</td>
                  <td>{house.feeMax}</td>
                  <td>{house.present}</td>
                  <td>{house.hits}</td>
                  <td>{house.statusType}</td>
                  <td>
                    <TextFormat type="date" value={house.createAt} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={house.updateAt} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{house.districtId ? <Link to={`district/${house.districtId}`}>{house.districtId}</Link> : ''}</td>
                  <td>{house.cityName ? <Link to={`city/${house.cityId}`}>{house.cityName}</Link> : ''}</td>
                  <td>{house.streetName ? <Link to={`street/${house.streetId}`}>{house.streetName}</Link> : ''}</td>
                  <td>{house.projectName ? <Link to={`landProject/${house.projectId}`}>{house.projectName}</Link> : ''}</td>
                  <td>{house.createByLogin ? house.createByLogin : ''}</td>
                  <td>{house.updateByLogin ? house.updateByLogin : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${house.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${house.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${house.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ house }: IRootState) => ({
  houseList: house.entities,
  totalItems: house.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(House);
