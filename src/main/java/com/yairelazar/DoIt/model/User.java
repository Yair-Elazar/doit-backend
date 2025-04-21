package com.yairelazar.DoIt.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // קונסטרקטור מותאם לשני שדות בלבד (ליצירת משתמש חדש)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
