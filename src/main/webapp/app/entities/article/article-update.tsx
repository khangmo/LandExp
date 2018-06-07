import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './article.reducer';
import { IArticle } from 'app/shared/model/article.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IArticleUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IArticleUpdateState {
  isNew: boolean;
  categoryId: number;
  createById: number;
  updateById: number;
}

export class ArticleUpdate extends React.Component<IArticleUpdateProps, IArticleUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      categoryId: 0,
      createById: 0,
      updateById: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCategories();
    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { article } = this.props;
      const entity = {
        ...article,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/article');
  };

  categoryUpdate = element => {
    const name = element.target.value.toString();
    if (name === '') {
      this.setState({
        categoryId: -1
      });
    } else {
      for (const i in this.props.categories) {
        if (name === this.props.categories[i].name.toString()) {
          this.setState({
            categoryId: this.props.categories[i].id
          });
        }
      }
    }
  };

  createByUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        createById: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            createById: this.props.users[i].id
          });
        }
      }
    }
  };

  updateByUpdate = element => {
    const login = element.target.value.toString();
    if (login === '') {
      this.setState({
        updateById: -1
      });
    } else {
      for (const i in this.props.users) {
        if (login === this.props.users[i].login.toString()) {
          this.setState({
            updateById: this.props.users[i].id
          });
        }
      }
    }
  };

  render() {
    const isInvalid = false;
    const { article, categories, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="landexpApp.article.home.createOrEditLabel">
              <Translate contentKey="landexpApp.article.home.createOrEditLabel">Create or edit a Article</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : article} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="article-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="title">
                    <Translate contentKey="landexpApp.article.title">Title</Translate>
                  </Label>
                  <AvField id="article-title" type="text" name="title" />
                </AvGroup>
                <AvGroup>
                  <Label id="titleAliasLabel" for="titleAlias">
                    <Translate contentKey="landexpApp.article.titleAlias">Title Alias</Translate>
                  </Label>
                  <AvField id="article-titleAlias" type="text" name="titleAlias" />
                </AvGroup>
                <AvGroup>
                  <Label id="summaryLabel" for="summary">
                    <Translate contentKey="landexpApp.article.summary">Summary</Translate>
                  </Label>
                  <AvField id="article-summary" type="text" name="summary" />
                </AvGroup>
                <AvGroup>
                  <Label id="contentLabel" for="content">
                    <Translate contentKey="landexpApp.article.content">Content</Translate>
                  </Label>
                  <AvField id="article-content" type="text" name="content" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusTypeLabel">
                    <Translate contentKey="landexpApp.article.statusType">Status Type</Translate>
                  </Label>
                  <AvInput
                    id="article-statusType"
                    type="select"
                    className="form-control"
                    name="statusType"
                    value={(!isNew && article.statusType) || 'PENDING'}
                  >
                    <option value="PENDING">PENDING</option>
                    <option value="PAID">PAID</option>
                    <option value="APPROVED">APPROVED</option>
                    <option value="CANCELED">CANCELED</option>
                    <option value="EXPIRED">EXPIRED</option>
                    <option value="SOLD">SOLD</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="hitsLabel" for="hits">
                    <Translate contentKey="landexpApp.article.hits">Hits</Translate>
                  </Label>
                  <AvField id="article-hits" type="number" className="form-control" name="hits" />
                </AvGroup>
                <AvGroup>
                  <Label id="createAtLabel" for="createAt">
                    <Translate contentKey="landexpApp.article.createAt">Create At</Translate>
                  </Label>
                  <AvField id="article-createAt" type="date" className="form-control" name="createAt" />
                </AvGroup>
                <AvGroup>
                  <Label id="updateAtLabel" for="updateAt">
                    <Translate contentKey="landexpApp.article.updateAt">Update At</Translate>
                  </Label>
                  <AvField id="article-updateAt" type="date" className="form-control" name="updateAt" />
                </AvGroup>
                <AvGroup>
                  <Label for="category.name">
                    <Translate contentKey="landexpApp.article.category">Category</Translate>
                  </Label>
                  <AvInput id="article-category" type="select" className="form-control" name="categoryId" onChange={this.categoryUpdate}>
                    <option value="" key="0" />
                    {categories
                      ? categories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="createBy.login">
                    <Translate contentKey="landexpApp.article.createBy">Create By</Translate>
                  </Label>
                  <AvInput id="article-createBy" type="select" className="form-control" name="createById" onChange={this.createByUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="updateBy.login">
                    <Translate contentKey="landexpApp.article.updateBy">Update By</Translate>
                  </Label>
                  <AvInput id="article-updateBy" type="select" className="form-control" name="updateById" onChange={this.updateByUpdate}>
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/article" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={isInvalid || updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  categories: storeState.category.entities,
  users: storeState.userManagement.users,
  article: storeState.article.entity,
  loading: storeState.article.loading,
  updating: storeState.article.updating
});

const mapDispatchToProps = {
  getCategories,
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ArticleUpdate);
