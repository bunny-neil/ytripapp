package com.ytripapp.domain;

import com.ytripapp.repository.support.index.CollectionOfEnumBridge;
import com.ytripapp.repository.support.index.LuceneSortBuilder;
import lombok.Data;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@Inheritance
@DiscriminatorColumn(name = "group_name")
public abstract class User implements Serializable {

    private static final long serialVersionUID = -7820800856438140841L;

    @SortableField(forField = "id" + LuceneSortBuilder.DEFAULT_POSTFIX)
    @Field(name = "id" + LuceneSortBuilder.DEFAULT_POSTFIX, store = Store.YES, index = Index.NO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Field(store = Store.YES, index = Index.NO)
    @Version
    Long version;

    @Field(store = Store.YES)
    boolean enabled = true;

    @SortableField
    @Field(store = Store.YES, analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.MILLISECOND)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated = new Date();

    @SortableField
    @Field(store = Store.YES, analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.MILLISECOND)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateUpdated;

    @SortableField
    @Field(store = Store.YES, analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.MILLISECOND)
    @Temporal(TemporalType.TIMESTAMP)
    Date dateLastSignIn;

    @SortableField
    @Field(store = Store.YES, analyze = Analyze.NO)
    @Transient
    Group group;

    @SortableField
    @Field(store = Store.YES, analyze = Analyze.NO)
    String emailAddress;

    @Field(store = Store.YES, analyze = Analyze.NO, index = Index.NO)
    String password;

    @Field(store = Store.YES, analyze = Analyze.NO)
    String stripeCustId;

    @Field(store = Store.YES, analyze = Analyze.NO, index = Index.NO)
    String apnsDeviceToken;

    @IndexedEmbedded
    @Embedded
    UserProfile profile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    List<SocialConnection> connections = new ArrayList<>();

    @Field(store = Store.YES, analyze = Analyze.NO)
    @FieldBridge(impl = CollectionOfEnumBridge.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    @CollectionTable(
        name = "users_authorities",
        joinColumns = @JoinColumn(name = "user_id")
    )
    Set<Authority> authorities = new HashSet<>();

}
