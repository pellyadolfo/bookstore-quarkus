package com.example.bookstore.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

//@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();
    
    // Could not use lombok here because recognised StackOverflow
    // https://terasolunaorg.github.io/guideline/5.0.0.RELEASE/en/Appendix/Lombok.html
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<CartItem> getItems() {
		return items;
	}

	public void setItems(Set<CartItem> items) {
		this.items = items;
	}
}
