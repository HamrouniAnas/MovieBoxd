describe("Test the movie Shop", () => {

  beforeEach(() => {
    cy.visit("http://localhost:9000");
    cy.get(".btn").click();
  })

  it("Create a new movie", () => {
  
    cy.get("#title").type("Stoker");
    cy.get("#director").type("Park Chan-Wook");
    cy.get("#quantity").type(3);
    cy.get("#genre").type("Thriller");
    cy.get("#price").type(20.99);

    cy.get('#submit-form').click();

    let new_row = cy.get("table").get("tr").last();

    new_row.contains('Stoker').should("be.visible");

  });

  it("Edit a movie", () => {

    cy.get(".edit").last().click();

    cy.get("#title").clear().type("Oldboy");
    cy.get("#director").clear().type("Park Chan-Wook");
    cy.get("#quantity").clear().type(1);
    cy.get("#genre").clear().type("Thriller");
    cy.get("#price").clear().type(20);

    cy.get('#submit-form').click();

    let new_row = cy.get("table").get("tr").last();

    new_row.contains('Oldboy').should("be.visible");

  });

  it("Delete a movie", () => {
    cy.get("table").find('tr').then(listing => {
      const listingCount = Cypress.$(listing).length;

      cy.get(".delete").last().click();
      cy.get("table").find('tr').should('have.length', listingCount-1)

    });
    

  });

});
