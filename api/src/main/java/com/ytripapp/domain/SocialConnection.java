package com.ytripapp.domain;

import com.ytripapp.repository.support.index.ParentDocumentBridge;
import lombok.Data;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "social_connections")
@Indexed(index = "connections")
public class SocialConnection implements Serializable {

    private static final long serialVersionUID = 5743192801325113523L;

    public enum ProviderName {
        Wechat,
        Facebook
    }

    @Id
    @DocumentId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @Enumerated(EnumType.STRING)
    ProviderName providerName;

    @Field(store = Store.YES, analyze = Analyze.NO)
    String connectionId;

    @Field(store = Store.YES, analyze = Analyze.NO)
    @FieldBridge(impl = ParentDocumentBridge.class)
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
