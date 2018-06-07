import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-financial.reducer';
import { IUserFinancial } from 'app/shared/model/user-financial.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserFinancialDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class UserFinancialDetail extends React.Component<IUserFinancialDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userFinancial } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.userFinancial.detail.title">UserFinancial</Translate> [<b>{userFinancial.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="housePrice">
                <Translate contentKey="landexpApp.userFinancial.housePrice">House Price</Translate>
              </span>
            </dt>
            <dd>{userFinancial.housePrice}</dd>
            <dt>
              <span id="houseAcreage">
                <Translate contentKey="landexpApp.userFinancial.houseAcreage">House Acreage</Translate>
              </span>
            </dt>
            <dd>{userFinancial.houseAcreage}</dd>
            <dt>
              <span id="loanRate">
                <Translate contentKey="landexpApp.userFinancial.loanRate">Loan Rate</Translate>
              </span>
            </dt>
            <dd>{userFinancial.loanRate}</dd>
            <dt>
              <span id="loanFromPeople">
                <Translate contentKey="landexpApp.userFinancial.loanFromPeople">Loan From People</Translate>
              </span>
            </dt>
            <dd>{userFinancial.loanFromPeople}</dd>
            <dt>
              <span id="customerMoneyHave">
                <Translate contentKey="landexpApp.userFinancial.customerMoneyHave">Customer Money Have</Translate>
              </span>
            </dt>
            <dd>{userFinancial.customerMoneyHave}</dd>
            <dt>
              <span id="customerMobile">
                <Translate contentKey="landexpApp.userFinancial.customerMobile">Customer Mobile</Translate>
              </span>
            </dt>
            <dd>{userFinancial.customerMobile}</dd>
            <dt>
              <span id="customerEmail">
                <Translate contentKey="landexpApp.userFinancial.customerEmail">Customer Email</Translate>
              </span>
            </dt>
            <dd>{userFinancial.customerEmail}</dd>
            <dt>
              <Translate contentKey="landexpApp.userFinancial.user">User</Translate>
            </dt>
            <dd>{userFinancial.userLogin ? userFinancial.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-financial" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-financial/${userFinancial.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userFinancial }: IRootState) => ({
  userFinancial: userFinancial.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserFinancialDetail);
