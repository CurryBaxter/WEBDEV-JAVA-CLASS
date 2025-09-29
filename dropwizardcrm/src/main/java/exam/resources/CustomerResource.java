package exam.resources;

import exam.api.Customer;
import exam.db.CustomerDAO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerDAO customerDAO;

    public CustomerResource(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @GET
    public List<Customer> getAllCustomers(@QueryParam("q") String query) {
        return customerDAO.search(query);
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") Long id) {
        return customerDAO.findById(id)
                .map(customer -> Response.ok(customer).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createCustomer(@NotNull @Valid Customer customer) {
        customer.setId(0);
        Customer newCustomer = customerDAO.save(customer);
        URI location = UriBuilder.fromResource(CustomerResource.class).path("{id}").build(newCustomer.getId());
        return Response.created(location).entity(newCustomer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, @NotNull @Valid Customer customer) {
        return customerDAO.findById(id)
                .map(existingCustomer -> {
                    customer.setId(id);
                    Customer updatedCustomer = customerDAO.save(customer);
                    return Response.ok(updatedCustomer).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        if (customerDAO.deleteById(id) > 0) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
