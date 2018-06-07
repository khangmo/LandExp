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
import { getSearchEntities, getEntities } from './user-subscription.reducer';
import { IUserSubscription } from 'app/shared/model/user-subscription.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IUserSubscriptionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IUserSubscriptionState extends IPaginationBaseState {
  search: string;
}

export class UserSubscription extends React.Component<IUserSubscriptionProps, IUserSubscriptionState> {
  state: IUserSubscriptionState = {
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
    const { userSubscriptionList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="user-subscription-heading">
          <Translate contentKey="landexpApp.userSubscription.home.title">User Subscriptions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="landexpApp.userSubscription.home.createLabel">Create new User Subscription</Translate>
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
                    placeholder={translate('landexpApp.userSubscription.home.search')}
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
                <th className="hand" onClick={this.sort('actionType')}>
                  <Translate contentKey="landexpApp.userSubscription.actionType">Action Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('keyword')}>
                  <Translate contentKey="landexpApp.userSubscription.keyword">Keyword</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('costFrom')}>
                  <Translate contentKey="landexpApp.userSubscription.costFrom">Cost From</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('costTo')}>
                  <Translate contentKey="landexpApp.userSubscription.costTo">Cost To</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('acreageFrom')}>
                  <Translate contentKey="landexpApp.userSubscription.acreageFrom">Acreage From</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('acreageTo')}>
                  <Translate contentKey="landexpApp.userSubscription.acreageTo">Acreage To</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('direction')}>
                  <Translate contentKey="landexpApp.userSubscription.direction">Direction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('floor')}>
                  <Translate contentKey="landexpApp.userSubscription.floor">Floor</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('bathRoom')}>
                  <Translate contentKey="landexpApp.userSubscription.bathRoom">Bath Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('bedRoom')}>
                  <Translate contentKey="landexpApp.userSubscription.bedRoom">Bed Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('parking')}>
                  <Translate contentKey="landexpApp.userSubscription.parking">Parking</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('landType')}>
                  <Translate contentKey="landexpApp.userSubscription.landType">Land Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('enabled')}>
                  <Translate contentKey="landexpApp.userSubscription.enabled">Enabled</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('createAt')}>
                  <Translate contentKey="landexpApp.userSubscription.createAt">Create At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('updateAt')}>
                  <Translate contentKey="landexpApp.userSubscription.updateAt">Update At</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.userSubscription.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.userSubscription.city">City</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="landexpApp.userSubscription.street">Street</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userSubscriptionList.map((userSubscription, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${userSubscription.id}`} color="link" size="sm">
                      {userSubscription.id}
                    </Button>
                  </td>
                  <td>{userSubscription.actionType}</td>
                  <td>{userSubscription.keyword}</td>
                  <td>{userSubscription.costFrom}</td>
                  <td>{userSubscription.costTo}</td>
                  <td>{userSubscription.acreageFrom}</td>
                  <td>{userSubscription.acreageTo}</td>
                  <td>{userSubscription.direction}</td>
                  <td>{userSubscription.floor}</td>
                  <td>{userSubscription.bathRoom}</td>
                  <td>{userSubscription.bedRoom}</td>
                  <td>{userSubscription.parking ? 'true' : 'false'}</td>
                  <td>{userSubscription.landType}</td>
                  <td>{userSubscription.enabled ? 'true' : 'false'}</td>
                  <td>
                    <TextFormat type="date" value={userSubscription.createAt} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={userSubscription.updateAt} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{userSubscription.userLogin ? userSubscription.userLogin : ''}</td>
                  <td>
                    {userSubscription.cityName ? <Link to={`city/${userSubscription.cityId}`}>{userSubscription.cityName}</Link> : ''}
                  </td>
                  <td>
                    {userSubscription.streetName ? (
                      <Link to={`street/${userSubscription.streetId}`}>{userSubscription.streetName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userSubscription.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userSubscription.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${userSubscription.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ userSubscription }: IRootState) => ({
  userSubscriptionList: userSubscription.entities,
  totalItems: userSubscription.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserSubscription);
