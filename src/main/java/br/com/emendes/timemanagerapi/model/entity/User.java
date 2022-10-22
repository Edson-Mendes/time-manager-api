package br.com.emendes.timemanagerapi.model.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "tb_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false, columnDefinition = "boolean DEFAULT true")
  private boolean enabled = true;
  @ManyToMany(fetch = FetchType.EAGER)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinTable(
      name = "tb_user_roles",
      joinColumns = @JoinColumn(name = "user_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "roles_id", nullable = false)
  )
  private Set<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (!Objects.equals(id, user.id)) return false;
    if (!Objects.equals(name, user.name)) return false;
    if (!Objects.equals(email, user.email)) return false;
    return Objects.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }

  public void addRole(Role newRole) {
    if(this.roles == null) this.roles = new HashSet<>();
    roles.add(newRole);
  }
}
